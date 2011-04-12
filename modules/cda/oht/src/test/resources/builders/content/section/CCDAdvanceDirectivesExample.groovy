package builders.content.section

ccd_advanceDirectives{
    text{
        table(border:'1', width:'100%'){
           thead{
               tr{
                   th('Directive')
                   th('Description')
                   th('Verification')
                   th('Supporting Document(s)')
               }
           }
           tbody{
               tr{
                   td('Resuscitation status')
                   td('Do not resuscitate')
                   td('Dr. Robert Dolin, Nov 07, 1999')
                   td{
                       linkHtml(href:'AdvanceDirective.b50b7910-7ffb-4f4c-bbe4-177ed68cbbf3.pdf','Advance directive')
                   }
               }
           }
        }
    }//text
	advanceDirectiveObservation{
		id(root:'9b54c3c9-1673-49c7-aef9-b037ed72ed27')
		code(code:'304251008', codeSystem:'2.16.840.1.113883.6.96', displayName:'Resuscitation')
		value(make{
		    cd(code:'304253006', codeSystem:'2.16.840.1.113883.6.96', displayName:'Do not resuscitate'){
		        originalText{
		            reference(value:'#AD1')
		        }
		    }
		})
		verifier{
            time(value:'19991107')
            participantRole{ 
                id(root:'20cf14fb-b65c-4c8c-a54d-b0cca834c18c') 
            }
        }//verifier
		advanceDirectiveStatus{
			value(code:'15240007', displayName:'Current and verified')
		}//advance directive observation status
		advanceDirectiveReference{
			id(root:'b50b7910-7ffb-4f4c-bbe4-177ed68cbbf3')
			code(code:'371538006',
					codeSystem:'2.16.840.1.113883.6.96',
					displayName:'Advance directive')
			text(mediaType:'application/pdf'){   
			    reference(value:'AdvanceDirective.b50b7910-7ffb-4f4c-bbe4-177ed68cbbf3.pdf') 
			}
		}//reference to external document
	}//advance directive observation
}//advance directive section
