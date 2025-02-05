package org.js.swiftcodes.service.dao.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;

import java.util.List;

@Mapper
public interface BankDataMapper {

    @Select("SELECT * FROM banks_data WHERE swift_code = #{swiftCode}")
    @Results(id = "bankDataResult", value = { @Result(property = "id", column = "id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),//
        @Result(property = "swiftCode", column = "swift_code", javaType = String.class, jdbcType = JdbcType.VARCHAR),//
        @Result(property = "countryIso2Code", column = "country_iso2_code", javaType = String.class, jdbcType = JdbcType.VARCHAR),//
        @Result(property = "isHeadquarter", column = "is_headquarter", javaType = Boolean.class, jdbcType = JdbcType.BOOLEAN),//
        @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),//
        @Result(property = "codeType", column = "code_type", javaType = String.class, jdbcType = JdbcType.VARCHAR),//
        @Result(property = "address", column = "address", javaType = String.class, jdbcType = JdbcType.VARCHAR),//
        @Result(property = "townName", column = "town_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),//
        @Result(property = "countryName", column = "country_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),//
        @Result(property = "timeZone", column = "time_zone", javaType = String.class, jdbcType = JdbcType.VARCHAR),//
        @Result(property = "parentId", column = "parent_id", javaType = Integer.class, jdbcType = JdbcType.INTEGER) })
    BankDataEntity selectOne(@Param("swiftCode") String swiftCode);

    @Select("SELECT * FROM banks_data")
    @ResultMap("bankDataResult")
    List<BankDataEntity> selectAll();

    @Insert({ """
        INSERT INTO banks_data (swift_code, country_iso2_code, is_headquarter, name, code_type, address, town_name, country_name, time_zone, parent_id)
        VALUES(#{bankData.swiftCode}, #{bankData.countryIso2Code}, #{bankData.isHeadquarter}, #{bankData.name}, #{bankData.codeType}, #{bankData.address}, #{bankData.townName}
              ,#{bankData.countryName}, #{bankData.timeZone}, #{bankData.parentId, jdbcType=INTEGER} )
        ON CONFLICT (swift_code) DO NOTHING;
        """ })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(@Param("bankData") BankDataEntity bankData);

    @Delete("DELETE FROM banks_data WHERE swift_code=#{swiftCode}")
    int deleteOne(String swiftCode);

}
