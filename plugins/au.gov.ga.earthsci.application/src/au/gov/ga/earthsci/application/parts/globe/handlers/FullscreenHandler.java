/*******************************************************************************
 * Copyright 2013 Geoscience Australia
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package au.gov.ga.earthsci.application.parts.globe.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MToolItem;
import org.eclipse.swt.widgets.Composite;

/**
 * Handler for the fullscreen command.
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 */
public class FullscreenHandler
{
	@Execute
	public void execute(Composite parent, MToolItem item)
	{
		boolean fullscreen = !parent.getShell().getFullScreen();
		parent.getShell().setFullScreen(fullscreen);
		item.setSelected(fullscreen);
	}
}