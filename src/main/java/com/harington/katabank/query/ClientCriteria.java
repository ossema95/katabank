package com.harington.katabank.query;

public class ClientCriteria {

	private static final String OVER = " > ";

	private static final String UNDER = " < ";

	private static final String NOT_IN = " not in ";

	private static final String IN = " in ";

	private static final String NOT_EQUAL = " != ";

	private static final String EQUAL = " = ";

	private static final String LIKE = " like ";
	
	private String criteriaString;
	private Class<?> propertyType;

	protected ClientCriteria() {
	}

	public void like(String value) {
		if (value != null) {
			this.criteriaString += LIKE + decorateValue("%"+value+"%");
			return;
		}
	}

	public void equalTo(String value) {
		if (value != null) {
			this.criteriaString += EQUAL + decorateValue(value);
			return;
		}
	}

	private String decorateValue(String value) {
		if (propertyType.isAssignableFrom(Number.class)) {
			return value;
		}
		return "'%s'".formatted(value);
	}

	public String getCriteriaString() {
		return criteriaString;
	}

	public void setCriteriaString(String criteriaString) {
		this.criteriaString = criteriaString;
	}

	protected ClientCriteria getClientCriteria(String propertyName, Class<?> type) {
		this.criteriaString = propertyName;
		this.propertyType = type;
		return this;
	}

	public void notEqual(String value) {
		if (value != null) {
			this.criteriaString += NOT_EQUAL + decorateValue(value);
			return;
		}
	}

	public void in(String... values) {
		if (values != null && values.length != 0) {
			this.criteriaString += IN + decorateIn(values);
			return;
		}
	}

	private String decorateIn(String[] values) {
		var sb = new StringBuilder();
		sb.append("(");
		for (int i = 0; i < values.length; i++) {
			sb.append(decorateValue(values[i]));
			if (i == values.length - 1) {
				// the last
				sb.append(") ");

			} else {
				sb.append(", ");
			}
		}
		return sb.toString();
	}

	public void notIn(String... values) {
		if (values != null && values.length != 0) {
			this.criteriaString += NOT_IN + decorateIn(values);
			return;
		}
	}

	public void under(String value) {
		if (value != null) {
			this.criteriaString += UNDER + decorateValue(value);
			return;
		}
	}
	
	public void over(String value) {
		if (value != null) {
			this.criteriaString += OVER + decorateValue(value);
			return;
		}
	}
}
