/*
 * generated by Xtext 2.15.0
 */
package fr.istic.ui.tests;

import com.google.inject.Injector;
import fr.istic.videogen.ui.internal.VideogenActivator;
import org.eclipse.xtext.testing.IInjectorProvider;

public class VideoGenUiInjectorProvider implements IInjectorProvider {

	@Override
	public Injector getInjector() {
		return VideogenActivator.getInstance().getInjector("fr.istic.VideoGen");
	}

}
