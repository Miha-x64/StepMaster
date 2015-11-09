# Step-by-step master in Android
StepMaster helps you to create a step-by-step master in Android.

## Usage
### One-liner
```java
new StepMaster(this).setSteps(R.id.steps).setButtons(true, true, true).create();
```
It will make a master that consists of `#steps`'s children. `#steps` can be a linear or relative layout — it is not important. setButtons(true, true, true) will create "Previous", "Next" and "Finish" button.

### More code
```java
sm = new StepMaster(this)
        .setSteps(R.id.steps) //A layout with steps.
                //#steps must be RelativeLayout, otherwise animations won't work correctly.
        .setButtons(R.array.masterButtons) //a string-array resource
        // Three elements of it are titles for Previous, Next, and Finish buttons.
        .setAnimations(
                R.anim.la_slide_left_out, R.anim.la_slide_left_in,
                R.anim.la_slide_right_out, R.anim.la_slide_right_in)
        // Animations. You can find them in /res/ directory.
        // Current step will swipe off the screen, displaced by another step.
        .setOnStepListener(new StepMaster.OnStepListener() {
            @Override
            public boolean onStepChange(
                int oldStep, int newStep, byte direction, final StepMaster master) {
                if (direction == StepMaster.DIRECTION_BACKWARD)
                    return true;
                // true == allow to change step
                // false == prevent master from changing step
                //here you can check something
                return true;
            }   // onStepChange
            @Override
            public void onStepChanged(int step, StepMaster master) {
                // you can observe what's going on
                currentStep = step;
            }
            @Override
            public void onFinish(StepMaster sm) {
                // actions when master is finished
            }
        })
        .create();
```

## Screenshots
![A step](https://dl.dropboxusercontent.com/u/50919212/GitHub/some_step.png)
![A final step](https://dl.dropboxusercontent.com/u/50919212/GitHub/final_step.png)
