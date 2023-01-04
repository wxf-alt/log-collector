package hive;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: wxf
 * @Date: 2022/12/13 19:37:21
 * @Description: MyUDTFFunction  返回 (事件名,事件字符串)
 * @Version 1.0.0
 */
@SuppressWarnings("ALL")
public class MyUDTFFunction extends GenericUDTF {

    @Override
    // 函数运行前 被调用一次。告诉MapTask，当前函数返回的结果类型和个数
    public StructObjectInspector initialize(ObjectInspector[] argOIs) throws UDFArgumentException {
        List<String> fieldName = new ArrayList<>();
        List<ObjectInspector> fieldOIs = new ArrayList<>();
        fieldName.add("event_name");
        fieldName.add("event_json");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldName, fieldOIs);
    }

    @Override
    // 处理数据 返回结果
    public void process(Object[] args) throws HiveException {
        // 检查参数
        if (null == args[0] || args.length == 0) {
            return;
        }
        try {
            // 构建JSON数组对象
            JSONArray jsonArray = new JSONArray(args[0].toString());
            // 查看 JSON 是否构建成功
            if (null == jsonArray || jsonArray.length() == 0) {
                return;
            }
            // 获取 JSON 中每一个{},再从每一个{}取出事件名
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    // 构建数组 存储输出数据
                    String[] result = new String[2];
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    // 获取 事件名
                    result[0] = jsonObject.getString("en");
                    // 获取 整个事件
                    result[1] = jsonObject.toString();
                    // 写出数据
                    forward(result);
                } catch (Exception e) {
                    // 继续遍历下一个 JSON 数组
                    continue;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    // 清理或者关闭操作
    public void close() throws HiveException {
    }

}