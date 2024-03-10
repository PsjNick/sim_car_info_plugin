# sim_car_info_plugin

获取手机电话卡信息的Flutter插件，暂时只支持Android

## 插件使用

```
 var info = await _simCarInfoPlugin.simCarInfo();

```
## 获取到的信息

获取到的信息为一个json数组字符串。

例：
[{"CarrierName":"CMCC — CMHK","CountryIso":"hk","DisplayName":"Mobile Duck","SimSlotIndex":0},{"CarrierName":"中国移动","CountryIso":"cn","DisplayName":"CMCC","SimSlotIndex":1,"Number":"+861389711****"}]


<br/>
## 字段解释
- CarrierName - 运营商名称
- CountryIso  - 国家代码
- DisplayName - 手机卡在设备上显示的名称
- SimSlotIndex - 手机卡槽位置 / 值> 0:卡槽1、值>1：卡槽2
- Number - 手机号码


<br/>

## ！！！注意
    Number 字段可能获取不到，原因为运营商可能未将手机号码存储到sim卡内。



