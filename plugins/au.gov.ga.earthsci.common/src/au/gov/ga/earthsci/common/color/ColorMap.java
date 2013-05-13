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
package au.gov.ga.earthsci.common.color;

import java.awt.Color;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import au.gov.ga.earthsci.worldwind.common.util.Util;

/**
 * An immutable colour map used for mapping values to colours according to a
 * colour table
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 * @author James Navin (james.navin@ga.gov.au)
 * 
 * @see au.gov.ga.earthsci.worldwind.common.util.ColorMap
 */
public class ColorMap
{

	public static enum InterpolationMode
	{
		/**
		 * Return the colour for the nearest matching value in the colour table
		 * to the value provided
		 * <p/>
		 * Useful for discretising continuous data.
		 */
		NEAREST_MATCH
		{
			@Override
			protected Color getColor(double value, TreeMap<Double, Color> entries, Color nodata)
			{
				Entry<Double, Color> ceiling = entries.ceilingEntry(value);
				Entry<Double, Color> floor = entries.floorEntry(value);

				if (ceiling == null && floor == null)
				{
					return nodata;
				}
				if (ceiling == null && floor != null)
				{
					return floor.getValue();
				}
				if (ceiling != null && floor == null)
				{
					return ceiling.getValue();
				}
				if (Math.abs(ceiling.getKey() - value) > Math.abs(floor.getKey() - value))
				{
					return floor.getValue();
				}
				return ceiling.getValue();
			}
		},

		/**
		 * Return the colour for the value in the colour table that exactly
		 * matches the value provided, or NODATA if none is found.
		 * <p/>
		 * Useful for classification colouring of discrete data.
		 */
		EXACT_MATCH
		{
			@Override
			protected Color getColor(double value, TreeMap<Double, Color> entries, Color nodata)
			{
				Color result = entries.get(value);
				if (result == null)
				{
					return nodata;
				}
				return result;
			}
		},

		/**
		 * Interpolate between the two closest colour entries by separately
		 * interpolating the RGB components of each colour.
		 */
		INTERPOLATE_RGB
		{
			@Override
			protected Color getColor(double value, TreeMap<Double, Color> entries, Color nodata)
			{
				return doInterpolate(value, entries, nodata, false);
			}
		},

		/**
		 * Interpolate between the two closest colour entries by interpolating
		 * along in HSB space, wrapping around the Hue axis where necessary, and
		 * converting back to RGB.
		 */
		INTERPOLATE_HUE
		{
			@Override
			protected Color getColor(double value, TreeMap<Double, Color> entries, Color nodata)
			{
				return doInterpolate(value, entries, nodata, true);
			}
		};

		protected abstract Color getColor(double value, TreeMap<Double, Color> entries, Color nodata);

		private static Color doInterpolate(double value, TreeMap<Double, Color> entries, Color nodata, boolean hue)
		{
			Entry<Double, Color> floor = entries.floorEntry(value);
			Entry<Double, Color> ceiling = entries.ceilingEntry(value);

			if (floor == null && ceiling == null)
			{
				return nodata;
			}

			double mixer = 0;
			if (floor != null && ceiling != null)
			{
				double window = ceiling.getKey() - floor.getKey();
				if (window > 0)
				{
					mixer = (value - floor.getKey()) / window;
				}
			}
			Color floorColor = floor == null ? null : floor.getValue();
			Color ceilingColor = ceiling == null ? null : ceiling.getValue();
			return Util.interpolateColor(floorColor, ceilingColor, mixer, hue);
		}
	}

	private static final Color DEFAULT_NODATA = new Color(0, 0, 0, 0);

	private final Color nodataColour;

	private final boolean valuesArePercentages;

	private final InterpolationMode mode;

	private TreeMap<Double, Color> entries = new TreeMap<Double, Color>();

	/**
	 * Create a new colour map using the provided entries. The instance will use
	 * RGB interpolation, will return {@code RGB(0,0,0,0)} for NODATA values and
	 * use absolute values as map entries.
	 * 
	 * @param entries
	 *            The colour map entries to use
	 */
	public ColorMap(Map<Double, Color> entries)
	{
		this(entries, DEFAULT_NODATA, InterpolationMode.INTERPOLATE_RGB, false);
	}

	/**
	 * Create a new fully configured colour map.
	 * 
	 * @param entries
	 *            The colour map entries to use
	 * @param nodataColour
	 */
	public ColorMap(Map<Double, Color> entries, Color nodataColour,
			InterpolationMode mode, boolean valuesArePercentages)
	{
		if (entries != null)
		{
			this.entries.putAll(entries);
		}
		this.nodataColour = nodataColour;
		this.mode = mode == null ? InterpolationMode.INTERPOLATE_RGB : mode;
		this.valuesArePercentages = valuesArePercentages;
	}


	/**
	 * Return the colour for the given value, using the appropriate
	 * interpolation mode.
	 * <p/>
	 * If {@link #isPercentageBased()}, expects a percentage value in the range
	 * {@code [0,1]} as input. Otherwise expects an absolute value.
	 * 
	 * @param value
	 *            The value to lookup in the map
	 * 
	 * @return The appropriate colour for the given value
	 */
	public Color getColor(double value)
	{
		return mode.getColor(value, entries, nodataColour);
	}

	/**
	 * Return the colour for the given absolute value, using the appropriate
	 * interpolation mode.
	 * <p/>
	 * If {@link #isPercentageBased()}, will calculate a percentage to use based
	 * on the {@code min} and {@code max} values. Otherwise will use the
	 * absolute value directly.
	 * 
	 * @param absoluteValue
	 *            The absolute data value to look up
	 * @param min
	 *            The minimum absolute value in the source data
	 * @param max
	 *            The maximum absolute value in the source data
	 * 
	 * @return The appropriate colour to use
	 */
	public Color getColor(double absoluteValue, double min, double max)
	{
		if (valuesArePercentages)
		{
			double percentage = (absoluteValue - Math.min(min, max)) / (Math.max(min, max) - Math.min(min, max));
			return getColor(percentage);
		}

		return getColor(absoluteValue);
	}

	/**
	 * @return the NODATA colour for this colour map
	 */
	public Color getNodataColour()
	{
		return nodataColour;
	}

	/**
	 * Return whether this colour map uses percentages in the range
	 * {@code [0,1]} rather than absolute values as keys in the map.
	 * 
	 * @return <code>true</code> if values are interpreted as percentages;
	 *         <code>false</code> otherwise.
	 */
	public boolean isPercentageBased()
	{
		return valuesArePercentages;
	}

	/**
	 * Return the interpolation mode being used by this colour map
	 * 
	 * @return the mode
	 */
	public InterpolationMode getMode()
	{
		return mode;
	}

	/**
	 * Return the entries in this colour map.
	 * 
	 * @return a read-only view of the entries in this colour map
	 */
	public Map<Double, Color> getEntries()
	{
		return Collections.unmodifiableMap(entries);
	}
}