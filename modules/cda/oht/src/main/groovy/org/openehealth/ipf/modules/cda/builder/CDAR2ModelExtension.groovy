/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.modules.cda.builder

import org.eclipse.emf.common.util.AbstractEnumerator
import org.eclipse.emf.ecore.util.FeatureMap
import org.eclipse.emf.ecore.util.FeatureMapUtil
import org.eclipse.emf.ecore.xmi.XMLResource
import org.eclipse.emf.ecore.xml.type.XMLTypePackage
import org.openehealth.ipf.modules.cda.CDAR2Renderer
import org.openhealthtools.ihe.common.cdar2.AD
import org.openhealthtools.ihe.common.cdar2.BL1
import org.openhealthtools.ihe.common.cdar2.CD
import org.openhealthtools.ihe.common.cdar2.ED
import org.openhealthtools.ihe.common.cdar2.EN
import org.openhealthtools.ihe.common.cdar2.II
import org.openhealthtools.ihe.common.cdar2.INT1
import org.openhealthtools.ihe.common.cdar2.POCDMT000040ClinicalDocument
import org.openhealthtools.ihe.common.cdar2.POCDMT000040RegionOfInterest
import org.openhealthtools.ihe.common.cdar2.PQ
import org.openhealthtools.ihe.common.cdar2.StrucDocCaption
import org.openhealthtools.ihe.common.cdar2.StrucDocContent
import org.openhealthtools.ihe.common.cdar2.StrucDocFootnote
import org.openhealthtools.ihe.common.cdar2.StrucDocItem
import org.openhealthtools.ihe.common.cdar2.StrucDocLinkHtml
import org.openhealthtools.ihe.common.cdar2.StrucDocParagraph
import org.openhealthtools.ihe.common.cdar2.StrucDocTd
import org.openhealthtools.ihe.common.cdar2.StrucDocText
import org.openhealthtools.ihe.common.cdar2.StrucDocTh
import org.openhealthtools.ihe.common.cdar2.StrucDocTitle
import org.openhealthtools.ihe.common.cdar2.StrucDocTitleContent
import org.openhealthtools.ihe.common.cdar2.StrucDocTitleFootnote
import org.openhealthtools.ihe.common.cdar2.StrucDocTr
import org.openhealthtools.ihe.common.cdar2.TS1

/**
 * @author Christian Ohr
 */
public class CDAR2ModelExtension extends BaseModelExtension {

    static private void setLiteralText(FeatureMap mixed, String s) {
        FeatureMapUtil.addText(mixed, s)
    }

    static private String getLiteralText(FeatureMap mixed) {
        getLiteralText(mixed, 0)
    }
     
     static private String getLiteralText(FeatureMap mixed, int index) {
         mixed ? mixed.get(XMLTypePackage.eINSTANCE.getXMLTypeDocumentRoot_Text(), true).get(index) : null
     }
     
	def register(List registered) {
	    
	    super.register(registered)
         
         // ----------------------------------------------------
         // Extensions for conveniently working with data types
         // ----------------------------------------------------         

         AD.metaClass {
             setText { String s	-> setLiteralText(delegate.mixed, s) }
             getText {			-> getLiteralText(delegate.mixed) }
         }
         
         BL1.metaClass {
             setText    { String s	-> delegate.value = Boolean.parseString(s) }
             setNumber  { Number n	-> delegate.value = (n == 0) }
         }
        
         CD.metaClass {
             setText { String s	-> delegate.code = s }
             getText { 			   -> delegate.code }
         }
         
         ED.metaClass {
             setText { String s	-> setLiteralText(delegate.mixed, s) }
             getText {			   -> getLiteralText(delegate.mixed) }
         }

         EN.metaClass {
             setText { String s	-> setLiteralText(delegate.mixed, s) }
             getText {			   -> getLiteralText(delegate.mixed) }
         }

         II.metaClass {
             setText { String s ->
                 def tokens = s?.tokenize('@')
                 if (tokens?.size() > 1) {
                   delegate.extension = tokens[0]
                   delegate.root = tokens[1]
                 } else if (tokens?.size() == 1) {
                   delegate.root = tokens[0]
                 }
             }
             getText { ->
                 if (delegate.extension) {
                    "${delegate.extension}@${delegate.root}"
                 } else {
                    delegate.root
                 }
             }
         }

         INT1.metaClass {
             setNumber  { Number n	-> delegate.value = n }
             getNumber	{           -> delegate.value }
         }

         PQ.metaClass {
             setText { String s	->
                 def tokens = s?.tokenize()
                 if (tokens?.size() > 1) {
                     delegate.value = Double.valueOf(tokens[0])
                     delegate.unit = tokens[1]
                 } else if (tokens?.size() == 1) {
                     delegate.value = Double.valueOf(tokens[0])
                     delegate.unit = '1'
                 }
             }
             getText {			->
                 "${delegate.value} ${delegate.unit}"
             }
             setNumber  { Number n	-> delegate.value = n.doubleValue() }
             getNumber	{           -> delegate.value }
         }
         

         TS1.metaClass {
             setText { String s	-> delegate.value = s }
             getText {			-> delegate.value }
         }

         /* Allow using ranges
         IVLTS.metaClass {
             
         }
         */
         
         AbstractEnumerator.metaClass {
             setText { String s	-> delegate.getByName(s) }
         }
         
         StrucDocText.metaClass {
             setText { String s	-> setLiteralText(delegate.mixed, s) }
             getText {			   -> getLiteralText(delegate.mixed) }
         }

         StrucDocTitle.metaClass {
             setText { String s	-> setLiteralText(delegate.mixed, s) }
             getText {			   -> getLiteralText(delegate.mixed) }
         }

        StrucDocCaption.metaClass {
            setText { String s	-> setLiteralText(delegate.mixed, s) }
            getText {			   -> getLiteralText(delegate.mixed) }
        }

        StrucDocContent.metaClass {
            setText { String s	-> setLiteralText(delegate.mixed, s) }
            getText {			   -> getLiteralText(delegate.mixed) }
        }

        StrucDocTitle.metaClass {
            setText { String s	-> setLiteralText(delegate.mixed, s) }
            getText {			   -> getLiteralText(delegate.mixed) }
        }

        StrucDocTitleContent.metaClass {
            setText { String s	-> setLiteralText(delegate.mixed, s) }
            getText {			   -> getLiteralText(delegate.mixed) }
        }

        StrucDocFootnote.metaClass {
            setText { String s	-> setLiteralText(delegate.mixed, s) }
            getText {			   -> getLiteralText(delegate.mixed) }
        }

        StrucDocTitleFootnote.metaClass {
            setText { String s	-> setLiteralText(delegate.mixed, s) }
            getText {			   -> getLiteralText(delegate.mixed) }
        }

        StrucDocItem.metaClass {
            setText { String s	-> setLiteralText(delegate.mixed, s) }
            getText {			   -> getLiteralText(delegate.mixed) }
        }

        StrucDocLinkHtml.metaClass {
            setText { String s	-> setLiteralText(delegate.mixed, s) }
            getText {			   -> getLiteralText(delegate.mixed) }
        }

        StrucDocParagraph.metaClass {
            setText { String s	-> setLiteralText(delegate.mixed, s) }
            getText {			   -> getLiteralText(delegate.mixed) }
        }

        StrucDocTd.metaClass {
            setText { String s	-> setLiteralText(delegate.mixed, s) }
            getText {			   -> getLiteralText(delegate.mixed) }
        }

        StrucDocTh.metaClass {
            setText { String s	-> setLiteralText(delegate.mixed, s) }
            getText {			   -> getLiteralText(delegate.mixed) }
        }

        StrucDocTr.metaClass {
            setText { String s	-> setLiteralText(delegate.mixed, s) }
            getText {			   -> getLiteralText(delegate.mixed) }
        }
        
         // TODO avoid renstantiation of renderer
         POCDMT000040ClinicalDocument.metaClass {

             getStructuredComponents { -> delegate.component.structuredBody.component }

             writeTo { Writer writer  -> 
                 def renderer = new CDAR2Renderer()
                 def opts = [:]
                 opts[XMLResource.OPTION_DECLARE_XML] = true
                 opts[XMLResource.OPTION_ENCODING] = 'utf-8'
                 renderer.render(delegate, writer, opts)
                 writer.flush()
             }
                       
         }
        
         POCDMT000040RegionOfInterest.metaClass {
             setID { String s -> delegate.setID1(s)}
             getID { -> getID1()}
         }

     }    


    String templateId() {
        extensionName()
    }

    String extensionName() {
        'Clinical Document Architecture (CDA)'
    }
    
}
