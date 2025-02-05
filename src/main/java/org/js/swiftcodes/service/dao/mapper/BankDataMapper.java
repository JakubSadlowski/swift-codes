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
import org.js.swiftcodes.service.exceptions.GeneralException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    //    @Insert("""
    //        WITH input_banks_data AS (
    //            <foreach collection='swiftCodes' item='item' separator='UNION ALL'>
    //                 SELECT #{item.id} as id
    //                      , #{item.swiftCode} as swift_code
    //                      , #{item.countryIso2Code} as country_iso2_code
    //                      , #{item.isHeadquarter} as is_headquarter
    //                      , #{item.name} as name
    //                      , #{item.codeType} as code_type
    //                      , #{item.address} as address
    //                      , #{item.townName} as town_name
    //                      , #{item.countryName} as country_name
    //                      , #{item.timeZone} as time_zone
    //                      , #{item.parentId} as parent_id
    //            </foreach>
    //        )
    //        INSERT INTO swift_codes (id, swift_code, country_iso2_code, is_headquarter, name, code_type, address, town_name, country_name, time_zone, parent_id)
    //        SELECT x.id, x.swift_code, x.country_iso2_code, x.is_headquarter, x.name
    //              ,x.code_type, x.address, x.town_name, x.country_name, x.time_zone, x.parent_id
    //          FROM input_swift_codes x
    //          LEFT JOIN swift_codes y ON x.swift_code = y.swift_code
    //         WHERE y.swift_code IS NULL;
    //        """)
    default void insertList(@Param("swiftCodes") List<BankDataEntity> swiftCodes) {
        Map<String, BankDataEntity> headquarters = firstInsertHeadquarters(swiftCodes);
        List<BankDataEntity> branches = getBranches(swiftCodes);

        for (BankDataEntity branch : branches) {
            String hqSwiftCode = getHqSwiftCode(branch);
            mapParentIdIfBranchMAtchesWithHq(branch, headquarters, hqSwiftCode);
            insert(branch);
        }
    }

    private static void mapParentIdIfBranchMAtchesWithHq(BankDataEntity branch, Map<String, BankDataEntity> headquarters, String hqSwiftCode) {
        BankDataEntity headquarter = headquarters.get(hqSwiftCode);
        if (headquarter != null) {
            branch.setParentId(headquarter.getId());
        }
    }

    private static String getHqSwiftCode(BankDataEntity branch) {
        return branch.getSwiftCode()
            .substring(0, 7);
    }

    private static List<BankDataEntity> getBranches(List<BankDataEntity> swiftCodes) {
        return swiftCodes.stream()
            .filter(b -> !b.isHeadquarter())
            .toList();
    }

    private Map<String, BankDataEntity> firstInsertHeadquarters(List<BankDataEntity> swiftCodes) {
        Map<String, BankDataEntity> headquarters = swiftCodes.stream()
            .filter(BankDataEntity::isHeadquarter)
            .collect(Collectors.toMap(BankDataEntity::getSwiftCode, Function.identity(), (key1, key2) -> {
                throw new GeneralException(String.format("duplicate key value found %s", key1));
            }));
        for (BankDataEntity bankDataEntity : headquarters.values()) {
            insert(bankDataEntity);
        }

        return headquarters;
    }

}
