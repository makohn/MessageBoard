package de.htwsaar.wirth.test;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.runner.SelectPackages;
import org.junit.runner.RunWith;

/**
 * Test-Suite zum Ausführen aller Tests
 * @author Oliver Seibert
 *
 */
@RunWith(JUnitPlatform.class)
@SelectPackages({ "de.htwsaar.wirth.test" })
public class RunTests {}
