package com.fabian57fabian.app.view;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;

import org.junit.runner.RunWith;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;

@RunWith(GUITestRunner.class)
public class IotSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;

	private IotSwingView iotSwingView;

	@Override
	protected void onSetUp() {
		GuiActionRunner.execute(() -> {
			iotSwingView = new IotSwingView();
			return iotSwingView;
		});
		window = new FrameFixture(robot(), iotSwingView);
		window.show(); // shows the frame to test
	}

	@Test
	@GUITest
	public void testControlsInitialStates() {
		window.label(JLabelMatcher.withText("id"));
		window.textBox("idTextBox").requireEnabled();
	}

}
