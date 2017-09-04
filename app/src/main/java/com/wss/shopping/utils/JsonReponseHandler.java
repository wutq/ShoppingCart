package com.wss.shopping.utils;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonReponseHandler
{

	/** listKey=pageEntity */
	public static <T> List<T> getListFromJsonWithPageEntity(Object response, Type type)
	{
		return getListFromJson(response, type, "pageEntity");
	}

	/**
	 * 将服务器返回的JSON转化为List，两层，
	 * 
	 * @param response
	 *            服务器的HTTP响应，JSON数据，之所以用object，是希望将msg.obj直接传送过来
	 * @param type
	 *            List的数据类型，如List<Comment>
	 *            更新ListView的适配器
	 *            进度对话框
	 * @author 陈福荣, 吴格非
	 */
	public static <T> List<T> getListFromJson(Object response, Type type, String listKey)
	{
		if (response == null)
		{
			return null;
		}
		JSONObject jo = null;
		if (response instanceof JSONObject)
		{
			jo = (JSONObject) response;
		} else if (response instanceof String)
		{
			try
			{
				jo = new JSONObject(response.toString());
			} catch (JSONException e)
			{
				e.printStackTrace();
			}
		}

		List<T> list = null;
		try
		{
			// 取list
			if (jo != null && !jo.equals(""))
			{
				String json = String.valueOf(jo.getJSONArray(listKey));
				Gson gson = new Gson();
				if (!"null".equals(json))
				{// 服务端可能返回null
					list = gson.fromJson(json, type);
				}
				if (list == null)
				{
					list = new ArrayList<T>();
				}
			}

		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		return list;
	}

}
