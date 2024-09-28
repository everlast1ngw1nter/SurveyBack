package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DbService {

    private final JdbcTemplate jdbcTemplateMap;

    public DbService(@Autowired JdbcTemplate jdbcTemplateMap) {
        this.jdbcTemplateMap = jdbcTemplateMap;
    }

    public String addUser(String email) {
        var id = String.valueOf(email.hashCode());
        try {
            jdbcTemplateMap.update(
                    "INSERT INTO " +
                            "users (id, email) " +
                            "VALUES (?, ?)",
                    id, email);
        } catch (DuplicateKeyException ignored) {

        }
        return id;
    }

//    public List<ScheduleDto> getAll() {
//        var rowSet = jdbcTemplateMap.queryForRowSet("SELECT * FROM schedule");
//        return convertToDto(rowSet);
//    }
//
//    public void deleteAll() {
//        jdbcTemplateMap.update("DELETE FROM schedule");
//    }
//
//    public LocalDate getMinAvailableDate() {
//        var rs = jdbcTemplateMap.queryForRowSet("SELECT MIN(date) FROM schedule");
//        rs.next();
//        try {
//            return rs.getTimestamp(1).toLocalDateTime().toLocalDate();
//        } catch (NullPointerException e) {
//            return LocalDate.of(1999, 12, 24);
//        }
//    }
//
//    private List<ScheduleDto> convertToDto(SqlRowSet rowSet) {
//        var listDto = new ArrayList<ScheduleDto>();
//        while (rowSet.next()) {
//            listDto.add(new ScheduleDto(
//                    rowSet.getInt("pair_number"),
//                    rowSet.getString("auditory"),
//                    rowSet.getDate("date").toLocalDate(),
//                    rowSet.getString("lesson"),
//                    rowSet.getString("lesson_type"),
//                    rowSet.getString("group_name"),
//                    rowSet.getString("teacher")
//            ));
//        }
//        return listDto;
//    }
}
