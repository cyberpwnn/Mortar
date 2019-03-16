package mortar.api.config;

import mortar.lang.json.JSONObject;

public interface Writable
{
	public void fromJSON(JSONObject j);

	public JSONObject toJSON();
}
