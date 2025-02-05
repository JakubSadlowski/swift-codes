package org.js.swiftcodes.service.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;

import java.util.List;
import java.util.stream.IntStream;

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
        @Result(property = "parentId", column = "parent_id", javaType = Integer.class, jdbcType = JdbcType.INTEGER) })
    List<BankDataEntity> fetchAllSwiftCodes();

    @Insert({
        "<script>",
        "WITH input_swift_codes AS (",
        "<foreach collection='swiftCodes' item='item' separator='UNION ALL'>",
        "SELECT ",
        "#{item.id, jdbcType=INTEGER} as id,",
        "    #{item.swiftCode, jdbcType=VARCHAR} as swift_code,",
        "    #{item.countryIso2Code, jdbcType=VARCHAR} as country_iso2_code,",
        "    #{item.isHeadquarter, jdbcType=BOOLEAN} as is_headquarter,",
        "    #{item.name, jdbcType=VARCHAR} as name,",
        "    #{item.codeType, jdbcType=VARCHAR} as code_type,",
        "    #{item.address, jdbcType=VARCHAR} as address,",
        "    #{item.townName, jdbcType=VARCHAR} as town_name,",
        "    #{item.countryName, jdbcType=VARCHAR} as country_name,",
        "    #{item.timeZone, jdbcType=VARCHAR} as time_zone,",
        "    #{item.parentId, jdbcType=INTEGER} as parent_id",
        "</foreach>",
        ")",
        "INSERT INTO swift_codes (",
        "    id, swift_code, country_iso2_code, is_headquarter, name, ",
        "    code_type, address, town_name, country_name, time_zone, parent_id",
        ")",
        "SELECT ",
        "    x.id, x.swift_code, x.country_iso2_code, x.is_headquarter, x.name,",
        "    x.code_type, x.address, x.town_name, x.country_name, x.time_zone, x.parent_id",
        "FROM input_swift_codes x",
        "LEFT JOIN swift_codes y ON x.swift_code = y.swift_code",
        "WHERE y.swift_code IS NULL",
        "</script>"
    })
    void insert(@Param("swiftCodes") List<BankDataEntity> swiftCodes);

    @Select("SELECT * FROM swift_codes WHERE swift_code = 'swiftCode'")
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
        @Result(property = "parentId", column = "parent_id", javaType = Integer.class, jdbcType = JdbcType.INTEGER) })
    BankDataEntity getSwiftCode(@Param("swiftCode") String swiftCode);


    default void storeAll(List<BankDataEntity> swiftCodes, int chunkSize){
        List<List<BankDataEntity>> lists = IntStream.range(0, (swiftCodes.size() + chunkSize - 1) / chunkSize)
            .mapToObj(i -> swiftCodes.subList(i * chunkSize, Math.min(swiftCodes.size(), (i + 1) * chunkSize)))
            .toList();
        for (List<BankDataEntity> smallerList : lists) {

            insert(smallerList);
        }
    }

}
