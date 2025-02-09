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
import org.apache.ibatis.annotations.Update;
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

    @Select("SELECT * FROM banks_data WHERE parent_id = #{parentId}")
    @ResultMap("bankDataResult")
    List<BankDataEntity> selectAllBranches(@Param("parentId") Integer parentId);

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

    @Select("""
        SELECT *
          FROM banks_data
         WHERE country_iso2_code = #{countryIso2Code}
         OREDR BY SUBSTRING(swift_code FROM '.{8}'), is_headquarter DESC
        """)
    @ResultMap("bankDataResult")
    List<BankDataEntity> selectBankDataByCountryISO2Code(@Param("countryIso2Code") String countryIso2Code);

    @Select("""
            SELECT id 
            FROM banks_data
            WHERE LEFT(swift_code, 8) = LEFT(#{swiftCode}, 8)
            AND swift_code LIKE '%XXX'
            LIMIT 1
        """)
    Integer selectHeadquartersId(@Param("swiftCode") String swiftCode);

    @Update("""
            UPDATE banks_data 
            SET parent_id = #{parentId}
            WHERE LEFT(swift_code, 8) = LEFT(#{swiftCode}, 8)
            AND swift_code NOT LIKE '%XXX'
        """)
    int updateBranchParentId(@Param("swiftCode") String swiftCode, @Param("parentId") Integer parentId);

    @Select("""
            SELECT swift_code 
            FROM banks_data
            WHERE LEFT(swift_code, 8) = LEFT(#{swiftCode}, 8)
            AND swift_code LIKE '%XXX'
            LIMIT 1
        """)
    String selectHeadquartersSwiftCode(@Param("swiftCode") String swiftCode);
}
