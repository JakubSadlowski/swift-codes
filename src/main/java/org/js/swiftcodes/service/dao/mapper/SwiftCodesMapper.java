package org.js.swiftcodes.service.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.js.swiftcodes.service.dao.entity.SwiftCodesEntity;

import java.util.List;

@Mapper
public interface SwiftCodesMapper {
    @Select("SELECT * FROM swift_codes")
    @Results({ @Result(property = "id", column = "id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),//
        @Result(property = "swiftCode", column = "swift_code", javaType = String.class, jdbcType = JdbcType.VARCHAR),//
        @Result(property = "countryIso2Code", column = "country_iso2_code", javaType = String.class, jdbcType = JdbcType.VARCHAR),//
        @Result(property = "isHeadquarter", column = "is_headquarter", javaType = Boolean.class, jdbcType = JdbcType.BOOLEAN),//
        @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),//
        @Result(property = "codeType", column = "code_type", javaType = String.class, jdbcType = JdbcType.VARCHAR),//
        @Result(property = "address", column = "address", javaType = String.class, jdbcType = JdbcType.VARCHAR),//
        @Result(property = "townName", column = "town_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),//
        @Result(property = "countryName", column = "country_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),//
        @Result(property = "timeZone", column = "time_zone", javaType = String.class, jdbcType = JdbcType.VARCHAR),//
        @Result(property = "timeZone", column = "parent_id", javaType = Integer.class, jdbcType = JdbcType.INTEGER) })
    List<SwiftCodesEntity> fetchAllSwiftCodes();
}
