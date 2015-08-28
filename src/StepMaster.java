package net.aquadc.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Mihail Goryunov on 09.08.15.
 * Makes a step-by-step master from a layout
 */
public class StepMaster {
    // UI
    private Activity activity;
    private Animation leaveForwardAnimation, comeForwardAnimation, leaveBackAnimation, comeBackAnimation;
    private ViewGroup[] steps;
    private ViewGroup buttonsHolder;
    private TextView previousButton, nextButton, finishButton;
    // Callback
    private OnStepListener changeListener;
    // State
    private int currentStep = 0;
    private boolean created = false;
    // Consts
    public static final byte DIRECTION_FORWARD = 1, DIRECTION_RANDOM = 0, DIRECTION_BACKWARD = -1;

    // INIT
    public StepMaster(Activity activity) {
        this.activity = activity;
    }

    // STEPS
    public StepMaster setSteps(ViewGroup... steps) {
        this.steps = steps;
        return this;
    }
    public StepMaster setSteps(int... stepsIds) {
        steps = new ViewGroup[stepsIds.length];
        for (byte b = 0; b < stepsIds.length; b++)
            steps[b] = (ViewGroup) activity.findViewById(stepsIds[b]);
        return this;
    }
    public StepMaster setSteps(ViewGroup stepsContainer) {
        int cnt = stepsContainer.getChildCount();
        steps = new ViewGroup[cnt];
        for (byte b = 0; b < cnt; b++)
            steps[b] = (ViewGroup) stepsContainer.getChildAt(b);
        return this;
    }
    public StepMaster setSteps(int stepsContainer) {
        setSteps((ViewGroup) activity.findViewById(stepsContainer));
        return this;
    }

    // BUTTONS
    public StepMaster setPreviousButton(TextView button) {
        previousButton = button;
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousStep();
            }
        });
        return this;
    }
    public StepMaster setPreviousButton(String text) {
        Button b = new Button(activity);
        b.setText(text);
        setPreviousButton(b);
        return this;
    }

    public StepMaster setNextButton(TextView button) {
        nextButton = button;
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextStep();
            }
        });
        return this;
    }
    public StepMaster setNextButton(String text) {
        Button b = new Button(activity);
        b.setText(text);
        setNextButton(b);
        return this;
    }

    public StepMaster setFinishButton(TextView button) {
        finishButton = button;
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeListener.onFinish(StepMaster.this);
            }
        });
        return this;
    }
    public StepMaster setFinishButton(String text) {
        Button b = new Button(activity);
        b.setText(text);
        setFinishButton(b);
        return this;
    }

    public StepMaster setButtons(TextView prev, TextView next) {
        previousButton = prev;
        nextButton = next;
        return this;
    }
    public StepMaster setButtons(String prev, String next) {
        setPreviousButton(prev);
        setNextButton(next);
        return this;
    }
    public StepMaster setButtons(int prev, int next) {
        Resources res = activity.getResources();
        setPreviousButton(res.getString(prev));
        setNextButton(res.getString(next));
        return this;
    }
    public StepMaster setButtons(TextView prev, TextView next, TextView finish) {
        previousButton = prev;
        nextButton = next;
        finishButton = finish;
        return this;
    }
    public StepMaster setButtons(String prev, String next, String finish) {
        setPreviousButton(prev);
        setNextButton(next);
        setFinishButton(finish);
        return this;
    }
    public StepMaster setButtons(int prev, int next, int finish) {
        Resources res = activity.getResources();
        setPreviousButton(res.getString(prev));
        setNextButton(res.getString(next));
        setFinishButton(res.getString(finish));
        return this;
    }
    public StepMaster setButtons(boolean prev, boolean next, boolean finish) {
        if (prev)   setPreviousButton("Previous");
        if (next)   setNextButton("Next");
        if (finish) setFinishButton("Finish");
        return this;
    }
    public StepMaster setButtons(int arrayId) {
        String[] strs = activity.getResources().getStringArray(arrayId);
        if (strs[0] != null)
            setPreviousButton(strs[0]);
        if (strs[1] != null)
            setNextButton(strs[1]);
        if (strs[2] != null)
            setFinishButton(strs[2]);
        return this;
    }

    // STEPS ANIMATIONS
    public StepMaster setLeaveForwardAnimation(Animation anim) {
        leaveForwardAnimation = anim;
        return this;
    }
    public StepMaster setLeaveForwardAnimation(int id) {
        leaveForwardAnimation = AnimationUtils.loadLayoutAnimation((Context) activity, id).getAnimation();
        return this;
    }

    public StepMaster setComeForwardAnimation(Animation anim) {
        comeForwardAnimation = anim;
        return this;
    }
    public StepMaster setComeForwardAnimation(int id) {
        comeForwardAnimation = AnimationUtils.loadLayoutAnimation((Context) activity, id).getAnimation();
        return this;
    }

    public StepMaster setLeaveBackAnimation(Animation anim) {
        leaveBackAnimation = anim;
        return this;
    }
    public StepMaster setLeaveBackAnimation(int id) {
        leaveBackAnimation = AnimationUtils.loadLayoutAnimation((Context) activity, id).getAnimation();
        return this;
    }

    public StepMaster setComeBackAnimation(Animation anim) {
        comeBackAnimation = anim;
        return this;
    }
    public StepMaster setComeBackAnimation(int id) {
        comeBackAnimation = AnimationUtils.loadLayoutAnimation((Context) activity, id).getAnimation();
        return this;
    }

    public StepMaster setAnimations(Animation leaveForward, Animation comeForward, Animation leaveBack, Animation comeBack) {
        leaveForwardAnimation = leaveForward;
        comeForwardAnimation = comeForward;
        leaveBackAnimation = leaveBack;
        comeBackAnimation = comeBack;
        return this;
    }
    public StepMaster setAnimations(int leaveForward, int comeForward, int leaveBack, int comeBack) {
        setLeaveForwardAnimation(leaveForward);
        setComeForwardAnimation(comeForward);
        setLeaveBackAnimation(leaveBack);
        setComeBackAnimation(comeBack);
        return this;
    }

    // FIRE!
    public StepMaster create() {
        if (steps.length == 0)
            return this;

        if (buttonsHolder == null) {
            buttonsHolder = new RelativeLayout(activity);
            buttonsHolder.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            RelativeLayout.LayoutParams nextBParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            if (android.os.Build.VERSION.SDK_INT >= 17)
                nextBParams.addRule(RelativeLayout.ALIGN_PARENT_END);
            else
                nextBParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            if (previousButton != null)
                buttonsHolder.addView(previousButton);
            if (nextButton != null) {
                buttonsHolder.addView(nextButton);
                nextButton.setLayoutParams(nextBParams);
            }
            if (finishButton != null) {
                buttonsHolder.addView(finishButton);
                finishButton.setLayoutParams(nextBParams);
                finishButton.setVisibility(View.GONE);
            }
        }

        for (View step : steps)
            step.setVisibility(View.GONE);

        created = true;
        setStep(currentStep, true);

        return this;
    }

    // GETTERS
    public TextView getPreviousButton(){
        return previousButton;
    }
    public TextView getNextButton(){
        return nextButton;
    }
    public TextView getFinishButton(){
        return finishButton;
    }

    // INTERACTIONS
    public void previousStep() {
        setStep(currentStep - 1);
    }

    public void nextStep() {
        setStep(currentStep + 1);
    }
    public void nextStep(boolean hardly) {
        setStep(currentStep + 1, hardly);
    }

    public StepMaster setStep(int step) {
        setStep(step, false);
        return this;
    }
    public void setStep(int step, boolean hardly) {
        setStep(step, hardly, false);
    }
    public void setStep(int step, boolean hardly, boolean notAnimate) {
        if (step < 0 || step >= steps.length)
            return;

        byte direction = determineDirection(currentStep, step);

        if (changeListener != null && !hardly)
            if (!changeListener.onStepChange(currentStep, step, direction, this))
                return;     // stop if listener returned false

        if (created) {
            if (steps[currentStep] != null) {
                hideCurrentStep(direction, notAnimate);
                steps[currentStep].removeView(buttonsHolder);
            }
            if (steps[step] != null) {
                showNewStep(direction, step, notAnimate);
            }
        }
        currentStep = step;

        if (changeListener != null)
            changeListener.onStepChanged(currentStep, this);
    }

    // LISTENER
    public interface OnStepListener {
        boolean onStepChange(int oldStep, int newStep, byte direction, StepMaster master);
        void onStepChanged(int newStep, StepMaster master);
        void onFinish(StepMaster master);
    }
    public StepMaster setOnStepChangeListener(OnStepListener listener) {
        this.changeListener = listener;
        return this;
    }

    // Private interactions
    private void hideCurrentStep(byte direction) {
        hideCurrentStep(direction, false);
    }
    private void showNewStep(byte direction, int step) {
        showNewStep(direction, step, false);
    }

    private void hideCurrentStep(byte direction, boolean notAnimate) {
        if (!notAnimate && leaveForwardAnimation != null && direction == DIRECTION_FORWARD) {
            final int oldStep = currentStep;
            leaveForwardAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationRepeat(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    steps[oldStep].setVisibility(View.GONE);
                }
            });
            steps[currentStep].startAnimation(leaveForwardAnimation);
        } else if (!notAnimate && leaveForwardAnimation != null && direction == DIRECTION_BACKWARD) {
            final int oldStep = currentStep;
            leaveBackAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationRepeat(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    steps[oldStep].setVisibility(View.GONE);
                }
            });
            steps[currentStep].startAnimation(leaveBackAnimation);
        } else
            steps[currentStep].setVisibility(View.GONE);
    }
    private void showNewStep(byte direction, int step, boolean notAnimate) {
        steps[step].addView(buttonsHolder);
        if (step == 0) {
            setVisibility(previousButton, View.GONE);
        }
        else
            setVisibility(previousButton, View.VISIBLE);
        if (step == steps.length-1) {
            setVisibility(nextButton, View.GONE);
            setVisibility(finishButton, View.VISIBLE);
        }
        else {
            setVisibility(nextButton, View.VISIBLE);
            setVisibility(finishButton, View.GONE);
        }
        if (!notAnimate && comeForwardAnimation != null && direction == DIRECTION_FORWARD) {
            steps[step].startAnimation(comeForwardAnimation);
        } else if (!notAnimate && comeForwardAnimation != null && direction == DIRECTION_BACKWARD) {
            steps[step].startAnimation(comeBackAnimation);
        }
        steps[step].setVisibility(View.VISIBLE);
    }
    private byte determineDirection(int currentStep, int step) {
        byte direction = DIRECTION_RANDOM;
        if (step == currentStep-1)
            direction = DIRECTION_BACKWARD;
        else if (step == currentStep+1)
            direction = DIRECTION_FORWARD;
        return direction;
    }
    private void setVisibility(View view, int vis) {
        if (view != null)
            view.setVisibility(vis);
    }
}
