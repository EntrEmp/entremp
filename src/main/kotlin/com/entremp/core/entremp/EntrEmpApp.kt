package com.entremp.core.entremp

import com.entremp.core.entremp.data.AttributesRepository
import com.entremp.core.entremp.data.CategoriesRepository
import com.entremp.core.entremp.data.CertificationTagsRepository
import com.entremp.core.entremp.model.Category
import com.entremp.core.entremp.model.Attribute
import com.entremp.core.entremp.model.CertificationTag
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class EntrEmpApp {
	private val logger = LoggerFactory.getLogger(EntrEmpApp::class.java)

	@Bean
	fun init(
		certificationTagsRepository: CertificationTagsRepository,
		categoriesRepository: CategoriesRepository,
		attributesRepository: AttributesRepository
	): CommandLineRunner = CommandLineRunner {

		val initialProductCertifications= arrayOf(
			CertificationTag(name = "IRAM"),
			CertificationTag(name = "ASTM"),
			CertificationTag(name = "API"),
			CertificationTag(name = "CE"),
			CertificationTag(name = "ACERTA")
		)

		val categories = mapOf(
			"Agriculture, Forestry, Fishing and Hunting" to listOf(
				"Crop Production",
				"Animal Production",
				"Forestry and Logging",
				"Fishing, Hunting and Trapping",
				"Support Activities for Agriculture and Forestry"
			),

			"Mining, Quarrying, and Oil and Gas Extraction" to listOf(
				"Oil and Gas Extraction",
				"Mining (except Oil and Gas)",
				"Support Activities for Mining"
			),

			"Construction" to listOf(
				"Construction of Buildings",
				"Heavy and Civil Engineering Construction",
				"Specialty Trade Contractors"
			),

			"Manufacturing" to listOf(
				"Food Manufacturing",
				"Beverage and Tobacco Product Manufacturing",
				"Textile Mills",
				"Textile Product Mills",
				"Apparel Manufacturing",
				"Leather and Allied Product Manufacturing",
				"Wood Product Manufacturing",
				"Paper Manufacturing",
				"Printing and Related Support Activities",
				"Petroleum and Coal Products Manufacturing",
				"Chemical Manufacturing",
				"Plastics and Rubber Products Manufacturing",
				"Nonmetallic Mineral Product Manufacturing",
				"Primary Metal Manufacturing",
				"Fabricated Metal Product Manufacturing",
				"Machinery Manufacturing",
				"Computer and Electronic Product Manufacturing",
				"Electrical Equipment, Appliance, and Component Manufacturing",
				"Transportation Equipment Manufacturing",
				"Furniture and Related Product Manufacturing",
				"Miscellaneous Manufacturing"
			),

			"Trade, Transportation, and Utilities" to listOf(
				"Merchant Wholesalers, Durable Goods"
			)
		)

		initialProductCertifications.forEach { tag ->
			if(certificationTagsRepository.findByName(tag.name) == null){
				logger.info("storing tag ${tag.name}")
				certificationTagsRepository.save(tag)
			}
		}

		categories.keys.forEach {name ->
			var category: Category? = categoriesRepository.findByName(name)
			if( category == null){
				logger.info("storing category $name")
				category = categoriesRepository.save(
					Category(
						name = name
					)
				)
			}

			categories[name]?.forEach { attributeName ->
				var attribute: Attribute? = attributesRepository.findByName(attributeName)
				if(attribute == null){
					logger.info("storing attribute $attributeName")
					attributesRepository.save(
						Attribute(
							category = category,
							name = attributeName
						)
					)
				}
			}
		}
	}

}

fun main(args: Array<String>) {
	runApplication<EntrEmpApp>(*args)
}
