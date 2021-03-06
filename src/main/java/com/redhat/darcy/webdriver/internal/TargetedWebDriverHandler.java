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

package com.redhat.darcy.webdriver.internal;

import com.redhat.darcy.util.ReflectionUtil;

import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * InvocationHandler for TargetedWebDriver proxies.
 * 
 * @see TargetedWebDriver
 * @see TargetedWebDriverFactory
 */
public class TargetedWebDriverHandler implements InvocationHandler {
    private final TargetLocator locator;
    private final WebDriverTarget target;
    
    /**
     * 
     * @param locator A {@link TargetLocator} for the parent "vanilla" 
     *                {@link org.openqa.selenium.WebDriver}. The proxy need not the driver, merely
     *                something that can switch the driver to the appropriate target.
     * @param target
     */
    public TargetedWebDriverHandler(TargetLocator locator, WebDriverTarget target) {
        this.locator = locator;
        this.target = target;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        switch (method.getName()) {
        case "switchTo":
            return locator;
        case "getWebDriverTarget":
            return target;
        case "createTargetedWebElement":
            List<Class<?>> interfaces = ReflectionUtil.getAllInterfaces(args[0]);

            return Proxy.newProxyInstance(
                    TargetedWebDriverHandler.class.getClassLoader(),
                    interfaces.toArray(new Class[interfaces.size()]),
                    new TargetedWebElementHandler((WebElement) args[0], locator, target));
        case "isPresent":
            try {
                target.switchTo(locator);
                return true;
            } catch (NoSuchWindowException | NoSuchFrameException e) {
                return false;
            }
        // Quitting a driver is not target specific
        case "quit":
            locator.defaultContent().quit();

            return null;
        }

        try {
            return method.invoke(target.switchTo(locator), args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
    
}
