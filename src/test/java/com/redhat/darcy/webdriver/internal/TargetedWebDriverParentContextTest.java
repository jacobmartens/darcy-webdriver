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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.redhat.darcy.web.api.Browser;
import com.redhat.darcy.web.api.Frame;
import com.redhat.darcy.webdriver.testing.rules.TraceTestName;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;

@RunWith(JUnit4.class)
public class TargetedWebDriverParentContextTest {
    @Rule
    public TraceTestName traceTestName = new TraceTestName();

    @Test
    public void shouldCreateTargetedDriversForBrowsers() {
        TargetedWebDriverFactory targetedWebDriverFactory =
                new CachingTargetedWebDriverFactory(mock(WebDriver.class),
                        WebDriverTargets.window("shouldn't-matter"));

        TargetedWebDriverParentContext targetedWebDriverParentContext =
                new TargetedWebDriverParentContext(mock(TargetedWebDriver.class),
                        targetedWebDriverFactory, mock(TargetedElementFactoryFactory.class));

        Browser browser = targetedWebDriverParentContext.findById(Browser.class, "test");

        WebDriver driver = ((WrapsDriver) browser).getWrappedDriver();
        assertThat(driver, instanceOf(TargetedWebDriver.class));

        TargetedWebDriver targetedDriver = (TargetedWebDriver) driver;

        assertEquals(WebDriverTargets.window("test"), targetedDriver.getWebDriverTarget());
    }

    @Test
    public void shouldCreateTargetedDriversForFrames() {
        TargetedWebDriverFactory targetedWebDriverFactory =
                new CachingTargetedWebDriverFactory(mock(WebDriver.class),
                        WebDriverTargets.window("parent"));

        TargetedWebDriverParentContext targetedWebDriverParentContext =
                new TargetedWebDriverParentContext(
                        targetedWebDriverFactory
                                .getTargetedWebDriver(WebDriverTargets.window("parent")),
                        targetedWebDriverFactory,
                        mock(TargetedElementFactoryFactory.class));

        Frame frame = targetedWebDriverParentContext.findById(Frame.class, "test");

        WebDriver driver = ((WrapsDriver) frame).getWrappedDriver();
        assertThat(driver, instanceOf(TargetedWebDriver.class));

        TargetedWebDriver targetedDriver = (TargetedWebDriver) driver;

        assertEquals(WebDriverTargets.frame(WebDriverTargets.window("parent"), "test"),
                targetedDriver.getWebDriverTarget());
    }
}
