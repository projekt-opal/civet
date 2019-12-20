package org.dice_research.opal.civet.metrics;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.tika.Tika;
import com.google.common.net.MediaType;
import java.io.File;
import java.io.IOException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

import com.google.common.net.MediaType;

/**
 * The DataFormatMetric gives a rating to a dataset based on informations provided 
 * about the mediaType/Format of all distributions.
 * 
 * @author Gourab Sahu
 */
public class DataFormatMetric implements Metric {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTION = 
			 " A dataset can have many distributions, we will calculate individual score for each distribution and"
			 +"finally add all these scores and calculate the averge score."
			 +" "
			 +"Check if all distributions in a dataset has valid mediaType(as per)IANA if yes then award 5 stars " 
			 +"Else Check if dcat:mediaType is a non-empty typed literal like \"csv\"^^dct:MediaTypeOrExtent then award 5 stars"
			 +"if dcat:mediaType does not satisfy above two conditions but contains a non-empty object then award 1 star "
			 +"Else award 0 stars to that distribution"
			 +"To Improve score:... "
			 +"Check if all distributions in a dataset has valid dct:format(as per)IANA if yes then award 5 stars remove bad oldscore"
			 +"if not then check dct:format is valid file extension, if yes remove bad score and give 5 stars "
			 +"Else check if an URI of \"http://publications.europa.eu/resource/authority/file-type/\" with valid file extension exist. "
			   + "If valid file extension then award 5 stars"
			 +"Else Check if dct:format has a non-empty typed literal like \"csv\"^^dct:MediaTypeOrExtent then award 5 stars"
			 +"Else check if dct:format is valid file extension like \"CSV\" or \"PDF\", if yes remove bad score and give 5 stars "
	         +"If dct:format does not satisfy any of the above condition but it is not empty then award 1 star"
			 +"if dct:format object is empty then 0 stars.";
           
	
	
	public static boolean ValidMediaType(String TypeToCheck) {

		boolean isValidFileType = false;

		try {
			MediaType mediatype = MediaType.parse(TypeToCheck);

			ArrayList<MediaType> ListOfValidMediaTypes = new ArrayList<MediaType>(Arrays.asList(

					/*
					 * This MediaTypes are from Google's open-source Guava Library Link:
					 * https://guava.dev/releases/20.0/api/docs/com/google/common/net/MediaType.html
					 */

					/*
					 * Why I am not using a wildcard(Like MediaType.AnyType) instead of manually
					 * typing all the MediaTypes ?
					 * 
					 * Answer: Yes, there are several wild cards. But these wildcards do not detect if
					 * there is any error in subType of the MediaType. For example, in folllowing
					 * code snippet if we change "sheet" to "shit"then it will still print 'True'.
					 * 
					 * final MediaType anyType = MediaType.ANY_APPLICATION_TYPE; MediaType tocheck =
					 * MediaType.parse(
					 * "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					 * System.out.println(tocheck.is(anyType));
					 * 
					 */

					MediaType.AAC_AUDIO, MediaType.APPLE_MOBILE_CONFIG, MediaType.APPLE_PASSBOOK,
					MediaType.APPLICATION_BINARY, MediaType.APPLICATION_XML_UTF_8, MediaType.ATOM_UTF_8,
					MediaType.BASIC_AUDIO, MediaType.BASIC_AUDIO, MediaType.BMP, MediaType.BZIP2,
					MediaType.CACHE_MANIFEST_UTF_8, MediaType.CRW, MediaType.CSS_UTF_8, MediaType.CSV_UTF_8,
					MediaType.DART_UTF_8, MediaType.EOT, MediaType.EPUB, MediaType.FLV_VIDEO, MediaType.FORM_DATA,
					MediaType.GIF, MediaType.GZIP, MediaType.HTML_UTF_8, MediaType.I_CALENDAR_UTF_8, MediaType.ICO,
					MediaType.JAVASCRIPT_UTF_8, MediaType.JPEG, MediaType.JSON_UTF_8, MediaType.KEY_ARCHIVE,
					MediaType.KML, MediaType.KMZ, MediaType.L24_AUDIO, MediaType.MANIFEST_JSON_UTF_8, MediaType.MBOX,
					MediaType.MICROSOFT_EXCEL, MediaType.MICROSOFT_POWERPOINT, MediaType.MICROSOFT_WORD,
					MediaType.MP4_AUDIO, MediaType.MP4_VIDEO, MediaType.MPEG_AUDIO, MediaType.MPEG_VIDEO,
					MediaType.NACL_APPLICATION, MediaType.NACL_PORTABLE_APPLICATION, MediaType.OCTET_STREAM,
					MediaType.OGG_AUDIO, MediaType.OGG_CONTAINER, MediaType.OGG_VIDEO, MediaType.OOXML_DOCUMENT,
					MediaType.OOXML_PRESENTATION, MediaType.OOXML_SHEET, MediaType.OPENDOCUMENT_GRAPHICS,
					MediaType.OPENDOCUMENT_PRESENTATION, MediaType.OPENDOCUMENT_SPREADSHEET,
					MediaType.OPENDOCUMENT_TEXT, MediaType.PDF, MediaType.PLAIN_TEXT_UTF_8, MediaType.PNG,
					MediaType.POSTSCRIPT, MediaType.PROTOBUF, MediaType.PSD, MediaType.QUICKTIME,
					MediaType.RDF_XML_UTF_8, MediaType.RTF_UTF_8, MediaType.SFNT, MediaType.SHOCKWAVE_FLASH,
					MediaType.SKETCHUP, MediaType.SOAP_XML_UTF_8, MediaType.SVG_UTF_8, MediaType.TAR,
					MediaType.TEXT_JAVASCRIPT_UTF_8, MediaType.THREE_GPP_VIDEO, MediaType.THREE_GPP2_VIDEO,
					MediaType.TIFF, MediaType.TSV_UTF_8, MediaType.VCARD_UTF_8, MediaType.VND_REAL_AUDIO,
					MediaType.VND_WAVE_AUDIO, MediaType.VORBIS_AUDIO, MediaType.VTT_UTF_8, MediaType.WAX_AUDIO,
					MediaType.WEBM_AUDIO, MediaType.WEBM_VIDEO, MediaType.WEBP, MediaType.WMA_AUDIO,
					MediaType.WML_UTF_8, MediaType.WMV, MediaType.WOFF, MediaType.WOFF2, MediaType.XHTML_UTF_8,
					MediaType.XML_UTF_8, MediaType.XRD_UTF_8, MediaType.ZIP));

			for (int counter = 0; counter < ListOfValidMediaTypes.size(); counter++) {
				// If MediaType matches then return True and exit.
				if (mediatype.is(ListOfValidMediaTypes.get(counter))) {
					isValidFileType = true;
				}
			}

		} catch (IllegalArgumentException e) {
			isValidFileType = false;
		}
		return isValidFileType;
	}

	// To check if dct:format has valid file extension if no IANA type found
		public static boolean ValidFileExtension(String extension) throws Exception {

			Tika formatChecker = new Tika();
			File fileToCheck = File.createTempFile("something", "." + extension);

			String DetectedFileType = formatChecker.detect(fileToCheck);
            
			/*
			 * As per https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Complete_list_of_MIME_types
			 * "application/octet-stream" means unknown file type.
			 */
			
			if (DetectedFileType.equals("application/octet-stream"))
				return false;
			else
				return true;
		}

	
	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {

		LOGGER.info("Processing dataset " + datasetUri);

		Resource dataset = ResourceFactory.createResource(datasetUri);

		// NodeIterator nodeIterator = model.listObjectsOfProperty(dataset,
		// DCAT.keyword);

		//Total number of distributions in a dataset.
				int TotalDistributions = 0;
				
				//Store a score for each distribution, we will use it for final evaluation.
				HashMap<String, Integer> DistributionsAndScores = new HashMap<String, Integer>();	
				
				/*
				 * A dataset can have many distributions. So first check each distributions for object of dct:MediaType whose 
				 * format should conform to IANA media format: https://www.iana.org/assignments/media-types/media-types.xhtml
				 * 
				 * To find out if the object of dct:mediaType conform to IANA, we will use "com.google.common.net.MediaType" class
				 * from Google's Guava Project. Link: https://guava.dev/releases/20.0/api/docs/com/google/common/net/MediaType.html
				 * This MediaType class conforms to IANA specifications as mentioned in RFCs 2045 . 
				 */
				NodeIterator DistributionsIterator = model.listObjectsOfProperty(dataset, DCAT.distribution);
					
					while(DistributionsIterator.hasNext()) {
						
						//Transform distribution objects into distribution resource
						Resource Distribution = (Resource) DistributionsIterator.nextNode();
						if(Distribution.hasProperty(DCAT.mediaType)) {
							
							//If distribution has valid MediaType(as per IANA) then award 5 stars else 1 star if not valid MediaType
							if(ValidMediaType(Distribution.getProperty(DCAT.mediaType).getObject().toString()))
								DistributionsAndScores.put(Distribution.toString(), 5);
							
							/*
							 * Check if a non-empty typed literal like "csv"^^dct:MediaTypeOrExtent. 
							 * If it has a valid file extension then award 5 stars, 1 star for invalid extension
							 * 0 stars for empty field.
							 */
							else if(!(Distribution.getProperty(DCAT.mediaType).getLiteral().getString().isEmpty())) {
								if(ValidFileExtension(Distribution.getProperty(DCAT.mediaType).getLiteral().getString().trim()))
									DistributionsAndScores.put(Distribution.toString(), 5);
								else
									DistributionsAndScores.put(Distribution.toString(), 1);
							}
							//else If object is not empty then award 5 star
							else if(!(Distribution.getProperty(DCAT.mediaType).getObject().toString().isEmpty()))
								DistributionsAndScores.put(Distribution.toString(), 1);
							else 
								//else 0 star for empty object
								DistributionsAndScores.put(Distribution.toString(), 0);
						}
						/*
						 * If the mediaType is absent or has bad score of 1 star because of invalid MediaType then check for 
						 * dct:format. If dct:format has valid fileFormat then award 5 stars else keep the previous score of 1 star.
						 */
						if(!Distribution.hasProperty(DCAT.mediaType) || DistributionsAndScores.get(Distribution.toString()) < 5)
						{		
							//1st check if it is valid IANA format, if yes award 5 stars
							if(Distribution.hasProperty(DCTerms.format)) {
								
								//If distribution has valid MediaType(as per IANA) then award 5 stars else 1 star if not valid MediaType
								if(ValidMediaType(Distribution.getProperty(DCTerms.format).getObject().toString()))
									DistributionsAndScores.put(Distribution.toString(), 5);

								/*
								 * Else check if an URI of "http://publications.europa.eu/resource/authority/file-type/" with valid
								 * file extension. If valid file extension then award 5 stars
								 */
								else if(Distribution.getProperty(DCTerms.format).getObject().
										toString().contains("http://publications.europa.eu/resource/authority/file-type/")) { 
									if(ValidFileExtension(Distribution.getProperty(DCTerms.format).getObject().
										toString().substring(59)))						
										DistributionsAndScores.put(Distribution.toString(), 5);
									else 
										DistributionsAndScores.put(Distribution.toString(), 1);
								}
								
								/*
								 * Check if a non-empty typed literal like "csv"^^dct:MediaTypeOrExtent. 
								 * If it has a valid file extension then award 5 stars, 1 star for invalid extension
								 * 0 stars for empty field.
								 */
								else if(!(Distribution.getProperty(DCTerms.format).getLiteral().getString().isEmpty())) {
									if(ValidFileExtension(Distribution.getProperty(DCTerms.format).getLiteral().getString().trim()))
										DistributionsAndScores.put(Distribution.toString(), 5);
									else
										DistributionsAndScores.put(Distribution.toString(), 1);
								}
								
								//Else if dct:format has valid extensions such as "CSV" or "PDF" then also award 5 stars. 
								else if(!(Distribution.getProperty(DCTerms.format).getObject().toString().isEmpty())) {
									if(ValidFileExtension(Distribution.getProperty(DCTerms.format).getObject().toString()))
										DistributionsAndScores.put(Distribution.toString(), 5);
									else
										DistributionsAndScores.put(Distribution.toString(), 1);
								}
							}

						}
						
					   //If both dct:format and dcat:mediatype are absent then give 0 stars to the distribution
						if(!(Distribution.hasProperty(DCTerms.format)) && !(Distribution.hasProperty(DCAT.mediaType)))
							DistributionsAndScores.put(Distribution.toString(), 0);
						
						//To calculate how many distributions in a dataset. It will be used for scoring.
						TotalDistributions++;

				}
				
				/**
				 * Score Evaluation:
				 * Total Number of distributions in Dataset = x
				 * Total aggregated scores of all distributions = y
				 * Overall Score = y/x
				 */
				float AggregatedScoreOfAllDistributions = 0;
				float OverallScore = 0;
				for (String key : DistributionsAndScores.keySet()) {
					AggregatedScoreOfAllDistributions+=DistributionsAndScores.get(key);
					System.out.println(key +":"+ DistributionsAndScores.get(key));
				}
				
				OverallScore = (float) Math.ceil(AggregatedScoreOfAllDistributions/TotalDistributions);
				

				return (int)(OverallScore);

	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_CATEGORIZATION.getURI();
	}

}
