/*
 Copyright 2014 Red Hat, Inc. and/or its affiliates.

 This file is part of darcy-webdriver.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.redhat.darcy.webdriver.elements;

import com.redhat.darcy.ui.api.elements.Link;
import com.redhat.darcy.webdriver.internal.ElementFactory;

import org.openqa.selenium.WebElement;

import java.util.function.Supplier;

public class WebDriverLink extends WebDriverElement implements Link {
    public WebDriverLink(Supplier<WebElement> source, ElementFactory elementFactory) {
        super(source, elementFactory);
    }

    @Override
    public void click() {
        getWrappedElement().click();
    }
    
    @Override
    public String getLinkText() {
        return getWrappedElement().getText();
    }

    @Override
    public boolean isEnabled() {
        return getWrappedElement().isEnabled();
    }
}
