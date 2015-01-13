package org.inovout.datastore.cloud.services.table.model;

import java.util.HashSet;
import java.util.Set;

import org.inovout.datastore.entity.metadata.model.Collection;
import org.inovout.datastore.entity.metadata.model.Property;
import org.inovout.util.TypeConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

public class TableInfo {
	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private Set<ColumnInfo> columns = new HashSet<ColumnInfo>();

	public Set<ColumnInfo> getColumns() {
		return this.columns;
	}

	public void setColumns(Set<ColumnInfo> columns) {
		this.columns = columns;
	}

	private static final ModelMapper MAPPER = new ModelMapper();
	static {
		MAPPER.addMappings(new PropertyMap<TableInfo, Collection>() {
			private TypeConverter<Set<ColumnInfo>, Set<Property>> converter = new TypeConverter<Set<ColumnInfo>, Set<Property>>() {
				@Override
				public Set<Property> convert(Set<ColumnInfo> source) {
					Set<Property> properties = new HashSet<Property>();
					for (ColumnInfo column : source) {
						properties.add(MAPPER.map(column, Property.class));
					}
					return properties;
				}
			};

			@Override
			protected void configure() {
				map().setProperties(converter.convert(source.getColumns()));
			}
		});
		MAPPER.addMappings(new PropertyMap<Collection, TableInfo>() {
			private TypeConverter<Set<Property>, Set<ColumnInfo>> converter = new TypeConverter<Set<Property>, Set<ColumnInfo>>() {
				@Override
				public Set<ColumnInfo> convert(Set<Property> source) {
					Set<ColumnInfo> columns = new HashSet<ColumnInfo>();
					for (Property property : source) {
						columns.add(MAPPER.map(property, ColumnInfo.class));
					}
					return columns;
				}
			};

			@Override
			protected void configure() {
				map().setColumns(converter.convert(source.getPropertires()));
			}
		});
		/*
		 * MAPPER.addConverter(new AbstractConverter<Set<Property>,
		 * Set<ColumnInfo>>() {
		 * 
		 * @Override protected Set<ColumnInfo> convert(Set<Property> source) {
		 * Set<ColumnInfo> columns = new HashSet<ColumnInfo>(); for (Property
		 * property : source) { columns.add(MAPPER.map(property,
		 * ColumnInfo.class)); } return columns; } });
		 */
/*		MAPPER.addConverter(new AbstractConverter<Set<ColumnInfo>, Set<Property>>() {
			@Override
			protected Set<Property> convert(Set<ColumnInfo> source) {
				Set<Property> properties = new HashSet<Property>();
				for (ColumnInfo column : source) {
					properties.add(MAPPER.map(column, Property.class));
				}
				return properties;
			}
		});*/
	}

	public Collection toCollection() {
		return MAPPER.map(this, Collection.class);
	}

	public static TableInfo fromCollection(Collection collection) {
		return MAPPER.map(collection, TableInfo.class);
	}
}