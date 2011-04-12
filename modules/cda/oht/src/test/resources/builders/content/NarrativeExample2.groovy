package builders.content

strucDocText {
	list {
		item {
			content(ID: 'a1', 'Asthma')
		}
		item {
			content(ID: 'a2', 'Hypertension (see HTN.cda for details)')
		}
		item {
			content(ID: 'a3', 'Osteoarthritis, ') {
				content(ID: 'a4', 'right knee')
			}
		}
	}
}

