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
package au.gov.ga.earthsci.editable;

import java.beans.IntrospectionException;

import org.eclipse.sapphire.ElementHandle;
import org.eclipse.sapphire.ElementType;
import org.eclipse.sapphire.ImpliedElementProperty;
import org.eclipse.sapphire.Property;
import org.eclipse.sapphire.Resource;
import org.eclipse.sapphire.modeling.ElementPropertyBinding;

import au.gov.ga.earthsci.editable.annotations.ElementBinder;

/**
 * {@link ElementBindingImpl} subclass used by the {@link EditableResource} as
 * the binding for {@link ImpliedElementProperty}s.
 * <p/>
 * Users can provide custom bindings that implement the {@link IElementBinder}
 * interface by adding the {@link ElementBinder} annotation to the
 * {@link ImpliedElementProperty} field in the element.
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 */
public class EditableImpliedElementBinding extends ElementPropertyBinding implements IRevertable
{
	private final Object parent;
	private final ElementHandle<?> property;
	private final Resource parentResource;
	private final IElementBinder<Object> binder;
	private EditableResource<?> resource;

	@SuppressWarnings("unchecked")
	public EditableImpliedElementBinding(Object parent, ElementHandle<?> property, Resource parentResource)
			throws InstantiationException, IllegalAccessException, IntrospectionException
	{
		this.parent = parent;
		this.property = property;
		this.parentResource = parentResource;

		ElementBinder binderAnnotation = property.definition().getAnnotation(ElementBinder.class);
		if (binderAnnotation != null && binderAnnotation.value() != null)
		{
			Class<? extends IElementBinder<?>> binderClass = binderAnnotation.value();
			binder = (IElementBinder<Object>) binderClass.newInstance();
		}
		else
		{
			binder = new BeanPropertyElementBinder();
		}
	}

	@Override
	public void init(Property property)
	{
		super.init(property);

		Object object = binder.get(parent, this.property, property.element());
		resource = new EditableResource<Object>(object, parentResource);
	}

	@Override
	public Resource read()
	{
		return resource;
	}

	@Override
	public ElementType type(Resource resource)
	{
		return property.definition().getType();
	}

	@Override
	public void revert()
	{
		resource.revert();
	}
}
