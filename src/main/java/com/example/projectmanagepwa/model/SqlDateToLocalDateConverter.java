package com.example.projectmanagepwa.model;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.sql.Date;
import java.time.LocalDate;

public class SqlDateToLocalDateConverter implements Converter<LocalDate, Date> {

    @Override
    public Result<Date> convertToModel(LocalDate localDate, ValueContext valueContext) {
        return localDate == null
                ? Result.ok(null)
                : Result.ok(Date.valueOf(localDate));
    }

    @Override
    public LocalDate convertToPresentation(Date date, ValueContext valueContext) {
        return date == null
                ? null
                : date.toLocalDate();
    }
}
