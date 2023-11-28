package com.example.cs361_assignment3_g11;
import android.app.Dialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;


public class FortuneActivity extends AppCompatActivity implements SensorEventListener {
    private static final int SHAKE_THRESHOLD = 10;
    private static final int INTERVAL_MS = 200;
    private static final int DELAY_AFTER_LAST_SHAKE = 2000;

    private SensorManager sensorManager;
    private PowerManager.WakeLock wl;
    private Handler handler = new Handler();
    private long lastShakeTime = 0;
    private int shakeCount = 0;
    private boolean isDialogDisplayed = false;

    private String[] fortuneMessages = {
            "🌟 Believe in yourself, and magic will follow.",
            "🌈 Your journey may have twists, but each turn brings new opportunities.",
            "🌺 Kindness is the key that unlocks doors to a brighter tomorrow.",
            "🚀 Adventure awaits you just beyond your comfort zone.",
            "🎉 Celebrate the small victories, for they pave the path to success.",
            "🌞 Let your smile be the sunshine that brightens someone's day.",
            "🌠 Stars can't shine without darkness. Embrace challenges and sparkle.",
            "🌱 Plant seeds of positivity, and watch your dreams grow.",
            "🌈 Your potential is limitless. Dare to dream big!",
            "🌟 Chase your goals with the passion of a shooting star.",
            "🦋 Transform setbacks into comebacks. You're resilient.",
            "🍀 Luck is on your side. Trust the process.",
            "🌈 Rainbows appear after storms. Better days are ahead.",
            "🌼 In every ending, a new beginning quietly unfolds.",
            "🚀 Boldly go where your heart leads. Adventure awaits!",
            "🌙 Even the moon goes through phases. Embrace your own cycles.",
            "🌺 Radiate positivity, and watch the world around you blossom.",
            "🌟 Miracles happen every day. Stay open to the magic.",
            "🎨 Life is your canvas. Paint it with vibrant colors.",
            "🌈 Dance in the rain and let joy wash over you.",
            "🌍 Explore the world, and discover the beauty within.",
            "🌈 Your uniqueness is your superpower. Own it.",
            "🎉 Celebrate the journey, not just the destination.",
            "🍀 Fortune favors the bold. Take that leap of faith.",
            "🌷 Like a flower, your beauty unfolds with time.",
            "🌈 Embrace change. It's the only constant in life.",
            "🌞 Your energy is contagious. Spread positivity.",
            "🚀 Shoot for the moon. Even if you miss, you'll land among stars.",
            "🌟 Every day is a gift. Unwrap it with gratitude.",
            "🌺 Bloom where you are planted. You're meant to thrive.",
            "🌈 Create your own sunshine on cloudy days.",
            "🌙 The night is darkest just before the dawn. Hold on.",
            "🎉 Confetti moments are hidden in everyday victories.",
            "🌍 The world is vast. Your possibilities are endless.",
            "🌟 Your potential is like a star—always shining, never fading.",
            "🌸 A kind word is a garden in the heart. Plant generously.",
            "🌈 Life is a journey, not a destination. Enjoy the ride.",
            "🌞 Rise and shine. Your brilliance lights up the world.",
            "🌟 Embrace imperfections; they add character to your story.",
            "🚀 Dream big, work hard, stay focused. Success will follow.",
            "🌈 Find joy in the ordinary. Life's magic is in the details.",
            "🌺 Your spirit is like a butterfly—beautiful and free.",
            "🌟 Radiate good vibes, and the universe will respond.",
            "🎉 Celebrate your progress, no matter how small.",
            "🌍 Leave a trail of kindness wherever you go.",
            "🌈 Stars can't shine without darkness. Embrace your light.",
            "🌞 Your potential is like a sunflower—vibrant and resilient.",
            "🌟 Dreams are the seeds of the future. Plant them wisely.",
            "🌷 Like a flower, you have the power to bloom anew each day.",
            "🚀 Take the scenic route. Life's beauty is in the detours.",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forture);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "myapp:SensorsInfoWakeLock");

        setupLottieAnimation();
    }

    private void setupLottieAnimation() {
        LottieAnimationView lottieAnimationView = findViewById(R.id.lottieAnimationView);
        lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);
        lottieAnimationView.playAnimation();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            detectShake(event.values[0], event.values[1], event.values[2]);
        }
    }

    private void detectShake(float x, float y, float z) {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - lastShakeTime) > INTERVAL_MS) {
            float acceleration = Math.abs(x + y + z - SensorManager.GRAVITY_EARTH);
            if (acceleration > SHAKE_THRESHOLD) {
                shakeCount++;
                updateShakeCountText(shakeCount);
                lastShakeTime = currentTime;

                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(shakeTimeoutRunnable, DELAY_AFTER_LAST_SHAKE);
            }
        }
    }

    private Runnable shakeTimeoutRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isDialogDisplayed) {
                showShakeDialog();
            }
        }
    };

    private void updateShakeCountText(int count) {
        TextView shakeCountTextView = findViewById(R.id.shakeCount);
        shakeCountTextView.setText("Your number is: " + count);
    }

    private void showShakeDialog() {
        isDialogDisplayed = true;
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_shake_count);

        TextView shakeNumCount = dialog.findViewById(R.id.shakeNumCount);
        shakeNumCount.setText("Your number is: " + shakeCount);
        TextView textSakeCount = dialog.findViewById(R.id.textShakeCount);
        if(shakeCount<=49){
            textSakeCount.setText(fortuneMessages[shakeCount]);
        }else {
            textSakeCount.setText(fortuneMessages[0]);
        }

        Button btnCloseDialog = dialog.findViewById(R.id.btnCloseDialog);
        btnCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                shakeCount = 0;
                isDialogDisplayed = false;
                updateShakeCountText(shakeCount);
            }
        });

        dialog.show();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        if (!wl.isHeld()) {
            wl.acquire();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);

        if (wl.isHeld()) {
            wl.release();
        }

        handler.removeCallbacksAndMessages(null);
    }
}