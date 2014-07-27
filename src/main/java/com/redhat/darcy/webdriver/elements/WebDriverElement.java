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

import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.api.elements.Findable;
import com.redhat.darcy.webdriver.internal.DefaultWebDriverElementContext;
import com.redhat.darcy.webdriver.internal.ElementFactory;

import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

public class WebDriverElement implements Element, WrapsElement {
    private final WebElement source;
    private final ElementFactory elementFactory;

    public WebDriverElement(WebElement source, ElementFactory elementFactory) {
        this.source = source;
        this.elementFactory = elementFactory;
    }

    @Override
    public boolean isDisplayed() {
        try {
            return getWrappedElement().isDisplayed();
        } catch (NotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean isPresent() {
        return ((Findable) source).isPresent();
    }

    @Override
    public WebElement getWrappedElement() {
        return source;
    }

    public ElementContext getElementContext() {
        // Make sure to look up the element each time so that it matches the cache
        return new DefaultWebDriverElementContext(getWrappedElement(), elementFactory);
    }
}
