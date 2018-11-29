package org.dice_research.opal.civet.webdemo;

/**
 * Constants for processing templates.
 *
 * @author Adrian Wilke
 */
public abstract class Templates {

	private final static String TEMPLATE_PATH = "webdemo/";

	public final static String HTML = TEMPLATE_PATH + "html.html";
	public final static String HTML_MARKER_TITLE = "<!--TITLE-->";
	public final static String HTML_MARKER_HEAD = "<!--HEAD-->";
	public final static String HTML_MARKER_BODY = "<!--BODY-->";

	public final static String START = TEMPLATE_PATH + "start.html";
	public final static String START_HTML_MARKER_ENDPOINT = "<!--ENDPOINT-->";
	public final static String START_HTML_MARKER_ENDPOINT_QUERIES = "<!--ENDPOINT_QUERIES-->";

}