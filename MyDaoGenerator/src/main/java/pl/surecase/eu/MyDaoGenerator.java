package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception
    {
        Schema schema = new Schema(7, "greendao");
        schema.setDefaultJavaPackageDao("com.splxtech.dao");
        Entity appliance = schema.addEntity("Appliance");
        appliance.addIdProperty();
        appliance.addStringProperty("name");
        appliance.addIntProperty("imageId1");
        appliance.addIntProperty("imageId2");
        appliance.addIntProperty("appId");
        appliance.addIntProperty("time");
        appliance.addIntProperty("waste");
        appliance.addBooleanProperty("online");
        Entity WasteTypeBean = schema.addEntity("WasteTypeBean");
        WasteTypeBean.implementsSerializable();
        WasteTypeBean.addIdProperty();
        WasteTypeBean.addIntProperty("DataYear");
        WasteTypeBean.addIntProperty("DataMouth");
        WasteTypeBean.addIntProperty("DataDay");
        WasteTypeBean.addIntProperty("UseNum");
        WasteTypeBean.addIntProperty("UseTime");
        WasteTypeBean.addIntProperty("PowerWaste");
        WasteTypeBean.addIntProperty("TimeWaste1");
        WasteTypeBean.addIntProperty("TimeWaste2");
        WasteTypeBean.addIntProperty("TimeWaste3");
        WasteTypeBean.addIntProperty("TimeWaste4");
        WasteTypeBean.addIntProperty("TimeWaste5");
        WasteTypeBean.addIntProperty("TimeWaste6");
        WasteTypeBean.addIntProperty("TimeWaste7");
        WasteTypeBean.addIntProperty("TimeWaste8");
        WasteTypeBean.addIntProperty("TimeWaste9");
        WasteTypeBean.addIntProperty("TimeWaste10");
        WasteTypeBean.addIntProperty("TimeWaste11");
        WasteTypeBean.addIntProperty("TimeWaste12");
        Property typeId = WasteTypeBean.addLongProperty("typeId").getProperty();
        WasteTypeBean.addToOne(appliance,typeId);
        ToMany addToMany = WasteTypeBean.addToMany(WasteTypeBean,typeId);
        addToMany.setName("wastes");
        new DaoGenerator().generateAll(schema, args[0]);
    }
}
