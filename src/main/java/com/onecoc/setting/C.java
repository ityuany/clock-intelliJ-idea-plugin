package com.onecoc.setting;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class C implements SearchableConfigurable {


    private Setting setting = new Setting();



    @NotNull
    @Override
    public String getId() {
        return "clock";
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "时钟";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return setting.getPanel1();
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }
}
