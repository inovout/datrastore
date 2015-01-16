package org.inovout.datastore.cloud.services.table.model;

import org.inovout.util.UnknownEnumExternalNameException;

public enum ColumnType {
	STRING("string"), INTEGER("integer"), BOOLEAN("boolean"), DOUBLE("double"), BINARY(
			"binary");

	private final String externalName;

	private ColumnType(String externalName) {
		this.externalName = externalName;
	}

	/**
	 * Get the corresponding externalized name for this value.
	 *
	 * @return The corresponding externalized name.
	 */
	public String getExternalName() {
		return externalName;
	}

	@Override
	public String toString() {
		return "AccessType[" + externalName + "]";
	}

	/**
	 * Resolve an AccessType from its external name.
	 *
	 * @param externalName
	 *            The external representation to resolve
	 *
	 * @return The access type.
	 *
	 * @throws UnknownEnumExternalNameException
	 *             If the externalName was not recognized.
	 *
	 * @see #getExternalName()
	 */
	public static ColumnType fromExternalName(String externalName) {
		if (externalName == null) {
			return null;
		}
		for (ColumnType propertyType : ColumnType.values()) {
			if (propertyType.getExternalName().equals(externalName)) {
				return propertyType;
			}
		}
		throw new UnknownEnumExternalNameException(externalName);
	}
}
