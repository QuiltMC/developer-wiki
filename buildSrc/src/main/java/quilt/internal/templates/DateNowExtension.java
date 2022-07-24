package quilt.internal.templates;

import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Extension;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class DateNowExtension extends AbstractExtension {
	public static Extension getExtension() {
		return new DateNowExtension();
	}

	@Override
	public Map<String, Object> getGlobalVariables() {
		return Collections.singletonMap("now", new Date());
	}
}
