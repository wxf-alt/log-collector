package hive;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

/**
 * @Auther: wxf
 * @Date: 2022/12/13 19:11:48
 * @Description: MyUDFFunction 返回匹配key的值
 * @Version 1.0.0
 */
// 需要提供多个 evaluate方法，evaluate不能返回void值。但是可以返回null
@SuppressWarnings("ALL")
public class MyUDFFunction extends UDF {

    // 传入Json和查询字段。返回该字段对应的值
    public String evaluate(String source, String param) throws JSONException {
        // 检查参数
        if (!source.contains(param) && !"ts".equals(param)) {
            return "";
        }
        String[] split = source.split("\\|");
        for (String s : split) {
            System.out.println(s);
        }
        // 构建Json对象
        JSONObject jsonObject = new JSONObject(split[1]);
        Object parse = JSON.parse(split[1]);

        // 获取时间戳
        if ("ts".equals(param)) {
            return split[0].trim();
        } else if ("ap".equals(param)) {
            return jsonObject.getString("ap");
        } else if ("et".equals(param)) {
            return jsonObject.getString("et");
        } else {
            // 获取 cm 中 某个属性
            JSONObject cm = jsonObject.getJSONObject("cm");
            if (cm.has(param)) {
                return jsonObject.getJSONObject("cm").getString(param);
            } else {
                throw new JSONException("没找到相关信息");
            }
        }
    }

    @Test
    public void testUdf() {
        String line = "1541217850324|{\"cm\":{\"mid\":\"m7856\",\"uid\":\"u8739\",\"ln\":\"-74.8\",\"sv\":\"V2.2.2\",\"os\":\"8.1.3\",\"g\":\"P7XC9126@gmail.com\",\"nw\":\"3G\",\"l\":\"es\",\"vc\":\"6\",\"hw\":\"640*960\",\"ar\":\"MX\",\"t\":\"1541204134250\",\"la\":\"-31.7\",\"md\":\"huawei-17\",\"vn\":\"1.1.2\",\"sr\":\"O\",\"ba\":\"Huawei\"},\"ap\":\"weather\",\"et\":[{\"ett\":\"1541146624055\",\"en\":\"display\",\"kv\":{\"goodsid\":\"n4195\",\"copyright\":\"ESPN\",\"content_provider\":\"CNN\",\"extend2\":\"5\",\"action\":\"2\",\"extend1\":\"2\",\"place\":\"3\",\"showtype\":\"2\",\"category\":\"72\",\"newstype\":\"5\"}},{\"ett\":\"1541213331817\",\"en\":\"loading\",\"kv\":{\"extend2\":\"\",\"loading_time\":\"15\",\"action\":\"3\",\"extend1\":\"\",\"type1\":\"\",\"type\":\"3\",\"loading_way\":\"1\"}},{\"ett\":\"1541126195645\",\"en\":\"ad\",\"kv\":{\"entry\":\"3\",\"show_style\":\"0\",\"action\":\"2\",\"detail\":\"325\",\"source\":\"4\",\"behavior\":\"2\",\"content\":\"1\",\"newstype\":\"5\"}},{\"ett\":\"1541202678812\",\"en\":\"notification\",\"kv\":{\"ap_time\":\"1541184614380\",\"action\":\"3\",\"type\":\"4\",\"content\":\"\"}},{\"ett\":\"1541194686688\",\"en\":\"active_background\",\"kv\":{\"active_source\":\"3\"}}]}";
        System.out.println(line);
        String x = "";
        try {
            x = evaluate(line, "ts");
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(x);
    }

}