/*******************************************************************************
 * Copyright 2016 Geoscience Australia
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
package au.gov.ga.earthsci.bookmark.ui.handlers;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.xml.sax.SAXException;

import au.gov.ga.earthsci.bookmark.io.BookmarksPersister;
import au.gov.ga.earthsci.bookmark.model.IBookmarkList;
import au.gov.ga.earthsci.bookmark.model.IBookmarks;
import au.gov.ga.earthsci.common.persistence.PersistenceException;

/**
 * Import a list of layers.
 *
 * @author Michael de Hoog
 */
public class ImportHandler
{
	@Inject
	private IBookmarks bookmarks;

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) throws IOException, SAXException,
			PersistenceException
	{
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		dialog.setFilterExtensions(new String[] { "*.xml" }); //$NON-NLS-1$
		dialog.setFilterNames(new String[] { "XML file" }); //$NON-NLS-1$
		String filename = dialog.open();

		if (filename == null)
		{
			return;
		}

		File file = new File(filename);
		IBookmarks loaded = BookmarksPersister.loadBookmarks(file, null);
		IBookmarkList[] lists = loaded.getLists();
		for (IBookmarkList list : lists)
		{
			IBookmarkList existingList = bookmarks.getListById(list.getId());
			if (existingList != null)
			{
				existingList.getBookmarks().addAll(list.getBookmarks());
			}
			else
			{
				bookmarks.addList(list);
			}
		}
	}
}
