/*******************************************************************************
 * Copyright 2012 Geoscience Australia
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
package au.gov.ga.earthsci.application.parts.info.handlers;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MToolItem;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import au.gov.ga.earthsci.application.parts.info.InfoPart;

/**
 * Link command handler for the info part.
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 */
public class LinkHandler
{
	private static final String TOOL_ITEM_ID = "au.gov.ga.earthsci.application.information.toolbar.link"; //$NON-NLS-1$
	private static final String PERSISTED_STATE_KEY =
			"au.gov.ga.earthsci.application.information.toolitems.link.persist"; //$NON-NLS-1$

	@Inject
	private EModelService service;

	@PostConstruct
	public void init(MPart part)
	{
		MToolItem toolItem = (MToolItem) service.find(TOOL_ITEM_ID, part.getToolbar());
		if (toolItem != null)
		{
			toolItem.setSelected(isLink(part));
		}
	}

	@Execute
	public void execute(MPart part, MToolItem item, InfoPart info)
	{
		setLink(part, item.isSelected());
		info.setLink(item.isSelected());
	}

	public static boolean isLink(MPart part)
	{
		String l = part.getPersistedState().get(PERSISTED_STATE_KEY);
		if (l == null)
		{
			return true;
		}
		return Boolean.parseBoolean(l);
	}

	public static void setLink(MPart part, boolean link)
	{
		part.getPersistedState().put(PERSISTED_STATE_KEY, String.valueOf(link));
	}
}
