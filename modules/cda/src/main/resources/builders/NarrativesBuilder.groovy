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
package builders

import org.openhealthtools.ihe.common.cdar2.*

strucDocHasCommonAttrs {
   properties {
      ID()
      language()
   }
   collections {
      styleCodes(collection:'styleCode') {
         styleCode()
      }
   }
}

strucDocText(schema:'strucDocHasCommonAttrs', factory:'STRUC_DOC_TEXT') {
   properties {
      mediaType()
   }
   collections {
      contents(collection:'content') {
         content(schema:'strucDocContent')
      }
      linkHtmls(collection:'linkHtml') {
         linkHtml(schema:'strucDocLinkHtml')
      }
      paragraphs(collection:'paragraph') {
         paragraph(schema:'strucDocParagraph')
      }
      lists(collection:'list') {
         list(schema:'strucDocList')
      }
      tables(collection:'table') {
         table(schema:'strucDocTable')
      }
      renderMultiMedias(collection:'renderMultiMedia') {
         renderMultiMedia(schema:'strucDocRenderMultiMedia')
      }
      subs(collection:'sub') {
         sub(schema:'strucDocSub')
      }
      sups(collection:'sup') {
         sup(schema:'strucDocSup')
      }
      brs(collection:'br') {
         br(schema:'strucDocBr')
      }
   }
}

strucDocTitle(schema:'strucDocHasCommonAttrs', factory:'STRUC_DOC_TEXT') {
   properties {
      mediaType()
   }
   collections {
      contents(collection:'content') {
         content(schema:'strucDocTitleContent')
      }
      footnotes(collection:'footnote') {
         footnote(schema:'strucDocTitleFootnote')
      }
      footnoteRefs(collection:'footnoteRef') {
         footnoteRef(schema:'strucDocFootnoteRef')
      }
      subs(collection:'sub') {
         sub(schema:'strucDocSub')
      }
      sups(collection:'sup') {
         sup(schema:'strucDocSup')
      }
      brs(collection:'br') {
         br(schema:'strucDocBr')
      }
   }
}

strucDocBr(factory:'STRUC_DOC_BR') {
}

strucDocCaption(schema:'strucDocHasCommonAttrs', factory:'STRUC_DOC_CAPTION') {
   collections {
      linkHtmls(collection:'linkHtml') {
         linkHtml(schema:'strucDocLinkHtml')
      }
      footnotes(collection:'footnote') {
         footnote(schema:'strucDocFootnote')
      }
      footnoteRefs(collection:'footnoteRef') {
         footnoteRef(schema:'strucDocFootnoteRef')
      }
      subs(collection:'sub') {
         sub(schema:'strucDocSub')
      }
      sups(collection:'sup') {
         sup(schema:'strucDocSup')
      }      
   }
}

strucDocCol(schema:'strucDocHasCommonAttrs', factory:'STRUC_DOC_COL') {
   properties {
      span(def:'1')
      width()
      align(factory:'ALIGN_TYPE7')
      _char(property:'char')
      charoff()
      valign(factory:'VALIGN_TYPE7')
   }
}

strucDocColgroup(schema:'strucDocCol', factory:'STRUC_DOC_COLGROUP') {
   properties {
      align(factory:'ALIGN_TYPE6')
      valign(factory:'VALIGN_TYPE')      
   }
   collections {
      cols(collection:'col') {
         col(schema:'strucDocCol')
      }
   }
}

strucDocContent(schema:'strucDocHasCommonAttrs', factory:'STRUC_DOC_CONTENT') {
   properties {
      revised(factory:'REVISED_TYPE')
   }
   collections {
      linkHtmls(collection:'linkHtml') {
         linkHtml(schema:'strucDocLinkHtml')
      }
      footnotes(collection:'footnote') {
         footnote(schema:'strucDocFootnote')
      }
      footnoteRefs(collection:'footnoteRef') {
         footnoteRef(schema:'strucDocFootnoteRef')
      }
      subs(collection:'sub') {
         sub(schema:'strucDocSub')
      }
      sups(collection:'sup') {
         sup(schema:'strucDocSup')
      }
      brs(collection:'br') {
         br(schema:'strucDocBr')
      }
      contents(collection:'content') {
         content(schema:'strucDocContent')
      }
      renderMultiMedias(collection:'renderMultiMedia') {
         renderMultiMedia(schema:'strucDocRenderMultiMedia')
      }
   }
}

strucDocTitleContent(schema:'strucDocHasCommonAttrs', factory:'STRUC_DOC_TITLE_CONTENT') {
   collections {
      footnotes(collection:'footnote') {
         footnote(schema:'strucDocTitleFootnote')
      }
      footnoteRefs(collection:'footnoteRef') {
         footnoteRef(schema:'strucDocFootnoteRef')
      }
      subs(collection:'sub') {
         sub(schema:'strucDocSub')
      }
      sups(collection:'sup') {
         sup(schema:'strucDocSup')
      }
      brs(collection:'br') {
         br(schema:'strucDocBr')
      }
      contents(collection:'content') {
         content(schema:'strucDocContent')
      }
   }
}

strucDocFootnote(schema:'strucDocHasCommonAttrs', factory:'STRUC_DOC_FOOTNOTE') {
   collections {
      linkHtmls(collection:'linkHtml') {
         linkHtml(schema:'strucDocLinkHtml')
      }
      lists(collection:'list') {
         list(schema:'strucDocList')
      }
      tables(collection:'table') {
         table(schema:'strucDocTable')
      }
      subs(collection:'sub') {
         sub(schema:'strucDocSub')
      }
      sups(collection:'sup') {
         sup(schema:'strucDocSup')
      }
      brs(collection:'br') {
         br(schema:'strucDocBr')
      }
      contents(collection:'content') {
         content(schema:'strucDocContent')
      }
      renderMultiMedias(collection:'renderMultiMedia') {
         renderMultiMedia(schema:'strucDocRenderMultiMedia')
      }
      paragraphs(collection:'paragraph') {
         paragraph(schema:'strucDocParagraph')
      }
   }
}

strucDocTitleFootnote(schema:'strucDocHasCommonAttrs', factory:'STRUC_DOC_TITLE_FOOTNOTE') {
   collections {
      subs(collection:'sub') {
         sub(schema:'strucDocSub')
      }
      sups(collection:'sup') {
         sup(schema:'strucDocSup')
      }
      brs(collection:'br') {
         br(schema:'strucDocBr')
      }
      contents(collection:'content') {
         content(schema:'strucDocContent')
      }      
   }
}

strucDocFootnoteRef(schema:'strucDocHasCommonAttrs', factory:'STRUC_DOC_FOOTNOTE_REF') {
   properties {
      idref()
   }
}

strucDocItem(schema:'strucDocHasCommonAttrs', factory:'STRUC_DOC_ITEM') {
   collections {
      linkHtmls(collection:'linkHtml') {
         linkHtml(schema:'strucDocLinkHtml')
      }
      lists(collection:'list') {
         list(schema:'strucDocList')
      }
      tables(collection:'table') {
         table(schema:'strucDocTable')
      }
      subs(collection:'sub') {
         sub(schema:'strucDocSub')
      }
      sups(collection:'sup') {
         sup(schema:'strucDocSup')
      }
      brs(collection:'br') {
         br(schema:'strucDocBr')
      }
      contents(collection:'content') {
         content(schema:'strucDocContent')
      }
      renderMultiMedias(collection:'renderMultiMedia') {
         renderMultiMedia(schema:'strucDocRenderMultiMedia')
      }
      paragraphs(collection:'paragraph') {
         paragraph(schema:'strucDocParagraph')
      }
      footnotes(collection:'footnote') {
         footnote(schema:'strucDocFootnote')
      }
      footnoteRefs(collection:'footnoteRef') {
         footnoteRef(schema:'strucDocFootnoteRef')
      }      
   }   
}

strucDocLinkHtml(schema:'strucDocHasCommonAttrs', factory:'STRUC_DOC_LINK_HTML') {
   properties {
      name()
      rel()
      rev()
      title()
      href()
   }
   collections {
      footnotes(collection:'footnote') {
         footnote(schema:'strucDocFootnote')
      }
      footnoteRefs(collection:'footnoteRef') {
         footnoteRef(schema:'strucDocFootnoteRef')
      }       
   }
}

strucDocList(schema:'strucDocHasCommonAttrs', factory:'STRUC_DOC_LIST') {
   properties {
      listType(factory:'LIST_TYPE_TYPE', def:ListTypeType.UNORDERED_LITERAL)
      caption(schema:'strucDocCaption')
   }
   collections {
      items(collection:'item', min:1) {
         item(schema:'strucDocItem')
      }
   }
}

strucDocParagraph(schema:'strucDocHasCommonAttrs', factory:'STRUC_DOC_PARAGRAPH') {
   properties {
      caption(schema:'strucDocCaption')
   }
   collections {
      linkHtmls(collection:'linkHtml') {
         linkHtml(schema:'strucDocLinkHtml')
      }
      subs(collection:'sub') {
         sub(schema:'strucDocSub')
      }
      sups(collection:'sup') {
         sup(schema:'strucDocSup')
      }
      brs(collection:'br') {
         br(schema:'strucDocBr')
      }
      contents(collection:'content') {
         content(schema:'strucDocContent')
      }
      renderMultiMedias(collection:'renderMultiMedia') {
         renderMultiMedia(schema:'strucDocRenderMultiMedia')
      }
      footnotes(collection:'footnote') {
         footnote(schema:'strucDocFootnote')
      }
      footnoteRefs(collection:'footnoteRef') {
         footnoteRef(schema:'strucDocFootnoteRef')
      }      
   }
}

strucDocRenderMultiMedia(schema:'strucDocHasCommonAttrs', factory:'STRUC_DOC_RENDER_MULTI_MEDIA') {
   properties {
      caption(schema:'strucDocCaption')
   }
   collections {
      referencedObjects(collection:'referencedObject') {
         referencedObject()
      }
   }
}

strucDocSub(factory:'STRUC_DOC_SUB') {
}

strucDocSup(factory:'STRUC_DOC_SUP') {
}

strucDocTable(schema:'strucDocHasCommonAttrs', factory:'STRUC_DOC_TABLE') {
   properties {
      summary()
      width()
      border()
      cellpadding()
      cellspacing()
      frame(factory:'FRAME_TYPE')
      rules(factory:'RULES_TYPE')
      caption(schema:'strucDocCaption')
      thead(schema:'strucDocTHead')
      tfoot(schema:'strucDocTFoot')
   }
   collections {
      // TODO: One of col and colgroup
      cols(collection:'col') {
         col(schema:'strucDocCol')
      }
      colgroups(collection:'colgroup') {
         colgroup(schema:'strucDocColgroup')
      }
      tbodys(collection:'tbody', min:1) {
         tbody(schema:'strucDocTBody')
      }
   }
}

strucDocTBody(schema:'strucDocHasCommonAttrs', factory:'STRUC_DOC_TBODY') {
   properties {
      align(factory:'ALIGN_TYPE')
      _char(property:'char')
      charoff()
      valign(factory:'VALIGN_TYPE6')
   }
   collections {
       trs(collection:'tr', min:1) {
          tr(schema:'strucDocTr')
       }
   }
}

strucDocTd(schema:'strucDocHasCommonAttrs', factory:'STRUC_DOC_TD') {
   properties {
      abbr()
      axis()
      scope(factory:'SCOPE_TYPE')
      rowspan(def:'1')
      colspan(def:'1')
      align(factory:'ALIGN_TYPE2')
      valign(factory:'VALIGN_TYPE1')
   }
   collections {
      linkHtmls(collection:'linkHtml') {
         linkHtml(schema:'strucDocLinkHtml')
      }
      subs(collection:'sub') {
         sub(schema:'strucDocSub')
      }
      sups(collection:'sup') {
         sup(schema:'strucDocSup')
      }
      brs(collection:'br') {
         br(schema:'strucDocBr')
      }
      contents(collection:'content') {
         content(schema:'strucDocContent')
      }
      renderMultiMedias(collection:'renderMultiMedia') {
         renderMultiMedia(schema:'strucDocRenderMultiMedia')
      }
      footnotes(collection:'footnote') {
         footnote(schema:'strucDocFootnote')
      }
      footnoteRefs(collection:'footnoteRef') {
         footnoteRef(schema:'strucDocFootnoteRef')
      }
      paragraphs(collection:'paragraph') {
         paragraph(schema:'strucDocParagraph')
      }
      lists(collection:'list') {
         list(schema:'strucDocList')
      }      
   }
}

strucDocTFoot(schema:'strucDocHasCommonAttrs', factory:'STRUC_DOC_TFOOT') {
   properties {
      align(factory:'ALIGN_TYPE1')
      _char(property:'char')
      charoff()
      valign(factory:'VALIGN_TYPE5')
   }
   collections {
       trs(collection:'tr', min:1) {
          tr(schema:'strucDocTr')
       }
   }
}

strucDocTh(schema:'strucDocHasCommonAttrs', factory:'STRUC_DOC_TH') {
   properties {
      abbr()
      axis()
      scope(factory:'SCOPE_TYPE1')
      rowspan(def:'1')
      colspan(def:'1')
      align(factory:'ALIGN_TYPE5')
      valign(factory:'VALIGN_TYPE3')
   }
   collections {
      linkHtmls(collection:'linkHtml') {
         linkHtml(schema:'strucDocLinkHtml')
      }
      subs(collection:'sub') {
         sub(schema:'strucDocSub')
      }
      sups(collection:'sup') {
         sup(schema:'strucDocSup')
      }
      brs(collection:'br') {
         br(schema:'strucDocBr')
      }
      contents(collection:'content') {
         content(schema:'strucDocContent')
      }
      renderMultiMedias(collection:'renderMultiMedia') {
         renderMultiMedia(schema:'strucDocRenderMultiMedia')
      }
      footnotes(collection:'footnote') {
         footnote(schema:'strucDocFootnote')
      }
      footnoteRefs(collection:'footnoteRef') {
         footnoteRef(schema:'strucDocFootnoteRef')
      }
   }
}

strucDocTHead(schema:'strucDocHasCommonAttrs', factory:'STRUC_DOC_THEAD') {
   properties {
      align(factory:'ALIGN_TYPE4')
      _char(property:'char')
      charoff()
      valign(factory:'VALIGN_TYPE4')
   }
   collections {
       trs(collection:'tr', min:1) {
          tr(schema:'strucDocTr')
       }
   }
}

strucDocTr(schema:'strucDocHasCommonAttrs', factory:'STRUC_DOC_TR') {
   properties {
      align(factory:'ALIGN_TYPE3')
      _char(property:'char')
      charoff()
      valign(factory:'VALIGN_TYPE2')
   }
   collections {
       //  TODO: either one, but at least once
       ths(collection:'th') {
          th(schema:'strucDocTh')
       }
       tds(collection:'td') {
          td(schema:'strucDocTd')
       }
   }
}


