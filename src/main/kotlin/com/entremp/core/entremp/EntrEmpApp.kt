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
			"Insumos agricolas" to listOf(
				"Semillas e hibridos",
				"Proteccion de cultivos",
				"Fertilizantes",
				"Otros Insumos agricolas",
				"Insumos para agricultura de precision",
				"Otros insumos agricolas"
			),

			"Insumos para ganaderia" to listOf(
				"Insumos veterinarios",
				"Nutricion animal",
				"Reproduccion y geneticas",
				"Otros insumos para ganaderia"
			),

			"Maquinaria agricola" to listOf(
				"Implementos agricolas",
				"Herramientas de labranza",
				"Equipamiento forrajero",
				"Otro equipamiento",
				"Transporte de Productos",
				"Maquinaria Frutihorticola"
			),

			"Maquinaria Pesada" to listOf(
				"Maquinaria Vial",
				"Maquinaria ferroviaria",
				"Otra maquinaria pesada"
			),

			"Repuestos Automotor" to listOf(
				"Neumaticos",
				"Bujias",
				"Otros repuestos automor"
			),

			"Mineria" to listOf(
				"Exploracion",
				"Explotacion",
				"Transporte",
				"Barras",
				"Explosivos",
				"Otros mineri"
			),

			"Petroleo y gas" to listOf(
				"Tubing",
				"Casing",
				"Sucker Rods",
				"Perforating Guns",
				"Tubos de conduccion",
				"Manometros",
				"Bombas presurizadoras",
				"Estaciones de Compresion",
				"Articulos para terminacion",
				"Otros petroleo y gas"
			),

			"Vehiculos y motos" to listOf(
				"Autos",
				"Camionetas",
				"Camiones",
				"Semiremolques",
				"Carrocerias",
				"Acoplados",
				"Motos",
				"Otros vehículos y motos"
			),

			"Construccion" to listOf(
				"Griferia",
				"Cemento y complementos",
				"Aberturas",
				"Aislamientos",
				"Escaleras y accesorios",
				"Hierro y acero",
				"Ladrillos  y bloques",
				"Cal",
				"quimicos",
				"Morteros",
				"Techos",
				"Acopio",
				"Aridos",
				"Pintura",
				"Viguetas",
				"Baldosones",
				"Refractarios",
				"Decoracion",
				"Refrigeracion",
				"Otros construccó"
			),

			"Bienes de consumo" to listOf(
				"Libreria y oficina",
				"Alimentos y bebidas",
				"Otros bienes de consumo"
			),

			"Electronica e informatica" to listOf(
				"Camaras y accesorios",
				"Sensores y actuadores",
				"Motores DC",
				"Motores AC",
				"Microelectronica",
				"Heladeras",
				"Electrodomesticos",
				"Drones",
				"Robotica",
				"Radiofrecuencia",
				"Cableado",
				"Herramientas electronica",
				"Telefonia",
				"Computadoras de escritorio",
				"Notebooks y netbooks",
				"Tablets",
				"PLC",
				"HIdraulicos",
				"Neumaticos",
				"Otros Electrónica e Informática"
			),

			"Textil" to listOf(
				"Indumentaria",
				"Bolsos y mochilas",
				"Ignifuga",
				"Estampados",
				"Calzado",
				"Tela por pieza",
				"Accesorios de moda",
				"Cuero e insumos mayoristas",
				"Otros textil"
			),

			"Salud y medicina" to listOf(
				"Insumos medicos",
				"Precursores quimicos",
				"Equipamiento dental",
				"Equipamiento Optica",
				"Radiologia",
				"Traumatologia",
				"Instrumental quirurgico",
				"Equipamiento enfermeria",
				"Suplementos medicinales",
				"Laboratiorio Medico",
				"Instrumental emergencia e internacion",
				"Diagnostico General",
				"Esterilizacion y sanidad",
				"Equipo optico y ultrasonico",
				"Farmaceutica",
				"Otros Salud y Medicina"
			),

			"Profesionales" to listOf(
				"Juridico y escribania",
				"Consultoria",
				"RRHH",
				"Otros Profesionales"
			),

			"Seguros" to listOf(
				"De vida",
				"ART",
				"Vehiculos",
				"Inmobiliarios",
				"Otros Seguros"
			),

			"Logistica y transporte" to listOf(
				"Nacional",
				"Internacional"
			),

			"Servicios de Almacenamiento" to emptyList(),
			"Servicios de Educacion" to emptyList(),
			"Servicios de Salud" to emptyList(),
			"Alojamiento y recreacion" to emptyList(),

			"Servicios Financieros" to listOf(
				"Creditos",
				"Bancarios",
				"Otros Servicios Financieros"
			),

			"Servicios Agrícolas" to listOf(
				"Acopio",
				"Otros Servicios agrícolas"
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
