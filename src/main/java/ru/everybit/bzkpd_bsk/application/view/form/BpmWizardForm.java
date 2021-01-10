package ru.everybit.bzkpd_bsk.application.view.form;

import com.vaadin.ui.Button;
import ru.everybit.bzkpd_bsk.application.view.BpmThemeStyles;

import java.util.HashMap;
import java.util.Map;

public abstract class BpmWizardForm extends BpmForm {
    public static final String CURRENT_WIZARD_FORM_STEP_NAME = "currentWizardFormStepName";
    private Map<String, BpmWizardFormStep> wizardFormStepMap;
    private Map<String, String> wizardFormStepOrderMap;
    private String firstWizardFormStepName;
    private String currentWizardFormStepName;
    private String lastWizardFormStepName;
    private Button nextButton;

    public BpmWizardForm() {
        super(false);
        wizardFormStepMap = new HashMap<>();
        wizardFormStepOrderMap = new HashMap<>();
    }

    @Override
    protected void initUi() {
        super.initUi();
        initWizardFormSteps();
        String wizardFormStepName = (String) getTaskVariable(CURRENT_WIZARD_FORM_STEP_NAME);
        if (wizardFormStepName == null) {
            wizardFormStepName = firstWizardFormStepName;
        }
        showWizardFormStep(wizardFormStepName);
    }

    protected abstract void initWizardFormSteps();

    public void addWizardFormStep(BpmWizardFormStep wizardFormStep) {
        String name = wizardFormStep.getName();
        wizardFormStepMap.put(name, wizardFormStep);
        if (firstWizardFormStepName == null) {
            firstWizardFormStepName = name;
        } else {
            wizardFormStepOrderMap.put(lastWizardFormStepName, name);
        }
        lastWizardFormStepName = name;
    }

    private void showWizardFormStep(String wizardFormStepName) {
        if (wizardFormStepName == null) {
            return;
        }
        nextButton = new Button();

        currentWizardFormStepName = wizardFormStepName;
        BpmWizardFormStep wizardFormStep = wizardFormStepMap.get(currentWizardFormStepName);
        wizardFormStep.init();

        formContentLayout.removeAllComponents();
        formContentLayout.addComponent(wizardFormStep.getView());

        nextButton.setCaption(wizardFormStepOrderMap.containsKey(currentWizardFormStepName) ? "Вперед" : "Завершить");
        nextButton.addStyleName(BpmThemeStyles.BUTTON_PRIMARY);
        nextButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                eventNextButtonClick();
            }
        });
        formContentLayout.addComponent(nextButton);
        buttonLayout.setVisible(false);
        setTaskVariable(CURRENT_WIZARD_FORM_STEP_NAME, currentWizardFormStepName);
    }

    private void eventNextButtonClick() {
        BpmWizardFormStep wizardFormStep = wizardFormStepMap.get(currentWizardFormStepName);
        wizardFormStep.eventNextButtonClick();
        if (!wizardFormStepOrderMap.containsKey(currentWizardFormStepName)) {
            submitButtonClick();
        } else {
            showWizardFormStep(wizardFormStepOrderMap.get(currentWizardFormStepName));
        }
    }

    public void setNextButtonEnabled(boolean enabled) {
        nextButton.setEnabled(enabled);
    }
}
