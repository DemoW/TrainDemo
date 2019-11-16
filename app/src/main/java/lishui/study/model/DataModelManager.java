package lishui.study.model;

/**
 * Created by lishui.lin on 19-11-15
 */
public class DataModelManager {

    public static DataModelManager modelManager;

    private final OfficialAccountModel officialAccountModel;

    private DataModelManager() {
        officialAccountModel = new OfficialAccountModel();
    }

    public synchronized static DataModelManager getInstance() {
        if (modelManager == null) {
            modelManager = new DataModelManager();
        }
        return modelManager;
    }
}
