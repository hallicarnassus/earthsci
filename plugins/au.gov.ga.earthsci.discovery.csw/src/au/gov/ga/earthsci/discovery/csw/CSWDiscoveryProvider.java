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
package au.gov.ga.earthsci.discovery.csw;

import java.net.URL;

import au.gov.ga.earthsci.discovery.IDiscoveryProvider;
import au.gov.ga.earthsci.discovery.IDiscoveryService;

/**
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 * 
 */
public class CSWDiscoveryProvider implements IDiscoveryProvider
{
	@Override
	public String getId()
	{
		// TODO Auto-generated method stub
		return "csw";
	}

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return "CSW";
	}

	@Override
	public IDiscoveryService createService(URL serviceURL)
	{
		return new CSWDiscoveryService(serviceURL, this);
	}
}
