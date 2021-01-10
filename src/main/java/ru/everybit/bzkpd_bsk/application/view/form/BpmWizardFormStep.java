package ru.everybit.bzkpd_bsk.application.view.form;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public abstract class BpmWizardFormStep {
    // data
    private String name;
    // view
    protected VerticalLayout wizardFormStepLayout;
    private BpmWizardForm wizardForm;

    public BpmWizardFormStep(String name, BpmWizardForm wizardForm) {
        this.name = name;
        this.wizardForm = wizardForm;
        wizardFormStepLayout = new VerticalLayout();
        wizardFormStepLayout.setSpacing(true);
    }

    public BpmWizardForm getForm() {
        return wizardForm;
    }

    public String getName() {
        return name;
    }

    public void init() {
        initModel();
        initView();
        initControl();
    }

    public void initModel() {
    }

    public void initView() {
    }

    public void initControl() {
    }

    public Component getView() {
        return wizardFormStepLayout;
    }

    public void eventNextButtonClick() {
    }

    protected void setNextButtonEnabled(boolean enabled) {
        wizardForm.setNextButtonEnabled(enabled);
    }
}
