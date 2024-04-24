CREATE DATABASE  IF NOT EXISTS `gticsbd` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `gticsbd`;
-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: gticsbd
-- ------------------------------------------------------
-- Server version	8.0.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `envios`
--

DROP TABLE IF EXISTS `envios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `envios` (
  `id_envios` int NOT NULL,
  `proveedor_id_proveedor` int NOT NULL,
  `descripcion` varchar(250) DEFAULT NULL,
  `fecha_registro` datetime DEFAULT NULL,
  `fecha_entrega` datetime DEFAULT NULL,
  PRIMARY KEY (`id_envios`),
  KEY `fk_envios_proveedor1_idx` (`proveedor_id_proveedor`),
  CONSTRAINT `fk_envios_proveedor1` FOREIGN KEY (`proveedor_id_proveedor`) REFERENCES `proveedor` (`id_proveedor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `envios`
--

LOCK TABLES `envios` WRITE;
/*!40000 ALTER TABLE `envios` DISABLE KEYS */;
/*!40000 ALTER TABLE `envios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicamentos`
--

DROP TABLE IF EXISTS `medicamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicamentos` (
  `id_medicamentos` int NOT NULL,
  `descripcion` varchar(250) DEFAULT NULL,
  `nombre` varchar(45) DEFAULT NULL,
  `foto` blob,
  `inventario` int DEFAULT NULL,
  `precio_unidad` double DEFAULT NULL,
  `fecha_ingreso` datetime DEFAULT NULL,
  PRIMARY KEY (`id_medicamentos`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicamentos`
--

LOCK TABLES `medicamentos` WRITE;
/*!40000 ALTER TABLE `medicamentos` DISABLE KEYS */;
INSERT INTO `medicamentos` VALUES (1,'Analgésico y antipirético ampliamente utilizado para aliviar el dolor leve a moderado y reducir la fiebre.','Paracetamol',NULL,200,5.99,'2024-04-11 08:00:00'),(2,'Analgésico y antiinflamatorio no esteroideo (AINE) comúnmente utilizado para aliviar el dolor, la inflamación y la fiebre.','Ibuprofeno',NULL,150,7.99,'2024-04-10 10:30:00'),(3,'Antibiótico para infecciones bacterianas','Amoxicilina',NULL,100,8.99,'2024-04-09 12:45:00'),(4,'Antihistamínico para alergias y rinitis.','Loratadina',NULL,120,9.99,'2024-04-08 14:15:00'),(5,'Benzodiazepina para trastornos de ansiedad y epilepsia','Clonazepam',NULL,80,10.99,'2024-04-07 16:20:00'),(6,'Inhibidor de la bomba de protones para el reflujo y úlceras','Omeprazol',NULL,90,6.99,'2024-04-06 18:25:00'),(7,'Antiviral para la influenza y gripe','Oseltamivir',NULL,50,12.99,'2024-04-05 20:40:00'),(8,'Anticoagulante para prevenir coágulos sanguíneos','Warfarina',NULL,70,15.99,'2024-04-04 22:00:00'),(9,'Antidepresivo para trastornos de ansiedad y depresión','Sertralina',NULL,110,11.99,'2024-04-03 09:10:00');
/*!40000 ALTER TABLE `medicamentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicamentos_has_pedidos`
--

DROP TABLE IF EXISTS `medicamentos_has_pedidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicamentos_has_pedidos` (
  `medicamentos_id_medicamentos` int NOT NULL,
  `pedidos_id_ventas` int NOT NULL,
  `pedidos_usuario_id_usuario` int NOT NULL,
  `cantidad` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`medicamentos_id_medicamentos`,`pedidos_id_ventas`,`pedidos_usuario_id_usuario`),
  KEY `fk_medicamentos_has_pedidos_pedidos2_idx` (`pedidos_id_ventas`,`pedidos_usuario_id_usuario`),
  KEY `fk_medicamentos_has_pedidos_medicamentos2_idx` (`medicamentos_id_medicamentos`),
  CONSTRAINT `fk_medicamentos_has_pedidos_medicamentos2` FOREIGN KEY (`medicamentos_id_medicamentos`) REFERENCES `medicamentos` (`id_medicamentos`),
  CONSTRAINT `fk_medicamentos_has_pedidos_pedidos2` FOREIGN KEY (`pedidos_id_ventas`, `pedidos_usuario_id_usuario`) REFERENCES `pedidos` (`id_ventas`, `usuario_id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicamentos_has_pedidos`
--

LOCK TABLES `medicamentos_has_pedidos` WRITE;
/*!40000 ALTER TABLE `medicamentos_has_pedidos` DISABLE KEYS */;
/*!40000 ALTER TABLE `medicamentos_has_pedidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedidos`
--

DROP TABLE IF EXISTS `pedidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedidos` (
  `id_ventas` int NOT NULL,
  `usuario_id_usuario` int NOT NULL,
  `fecha_solicitud` datetime DEFAULT NULL,
  `costo_total` double DEFAULT NULL,
  `estado_del_pedido` varchar(45) DEFAULT NULL,
  `tipo` varchar(45) DEFAULT NULL,
  `receta` blob,
  `fecha_entrega` datetime DEFAULT NULL,
  `comentario` varchar(45) DEFAULT NULL,
  `fecha_aprobacion` datetime DEFAULT NULL,
  `medico_que_atiende` varchar(45) DEFAULT NULL,
  `envios_id_envios` int NOT NULL,
  PRIMARY KEY (`id_ventas`,`usuario_id_usuario`,`envios_id_envios`),
  KEY `fk_ventas_usuario1_idx` (`usuario_id_usuario`),
  KEY `fk_pedidos_envios1_idx` (`envios_id_envios`),
  CONSTRAINT `fk_pedidos_envios1` FOREIGN KEY (`envios_id_envios`) REFERENCES `envios` (`id_envios`),
  CONSTRAINT `fk_ventas_usuario1` FOREIGN KEY (`usuario_id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedidos`
--

LOCK TABLES `pedidos` WRITE;
/*!40000 ALTER TABLE `pedidos` DISABLE KEYS */;
INSERT INTO `pedidos` VALUES (1,3,'2024-04-24 19:11:22',50.99,'Solicitado',NULL,NULL,NULL,NULL,'2024-04-24 19:11:22','Juan',1),(2,5,'2024-04-24 19:11:22',39.99,'En Proceso',NULL,NULL,NULL,NULL,'2024-04-24 19:11:22','Maria',2),(3,7,'2024-04-24 19:11:22',29.99,'Empaquetado',NULL,NULL,NULL,NULL,'2024-04-24 19:11:22','Carlos',3),(4,9,'2024-04-24 19:11:22',19.99,'En Ruta',NULL,NULL,NULL,NULL,'2024-04-24 19:11:22','Ana',4),(5,11,'2024-04-24 19:11:22',59.99,'Entregado',NULL,NULL,'2024-04-24 19:11:22',NULL,'2024-04-24 19:11:22','Pedro',5),(6,13,'2024-04-24 19:11:22',69.99,'Solicitado',NULL,NULL,'2024-04-24 19:11:22',NULL,'2024-04-24 19:11:22','Laura',6),(7,15,'2024-04-24 19:11:22',79.99,'En Proceso',NULL,NULL,'2024-04-24 19:11:22',NULL,'2024-04-24 19:11:22','Diego',7),(8,17,'2024-04-24 19:11:22',89.99,'Empaquetado',NULL,NULL,'2024-04-24 19:11:22',NULL,'2024-04-24 19:11:22','Sofia',8),(9,19,'2024-04-24 19:11:22',99.99,'En Ruta',NULL,NULL,'2024-04-24 19:11:22',NULL,'2024-04-24 19:11:22','Julia',9),(10,21,'2024-04-24 19:11:22',109.99,'Entregado',NULL,NULL,'2024-04-24 19:11:22',NULL,'2024-04-24 19:11:22','Alejandro',10);
/*!40000 ALTER TABLE `pedidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proveedor`
--

DROP TABLE IF EXISTS `proveedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proveedor` (
  `id_proveedor` int NOT NULL,
  `nombre` varchar(45) DEFAULT NULL,
  `descripcion` varchar(250) DEFAULT NULL,
  `proveedorcol` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id_proveedor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proveedor`
--

LOCK TABLES `proveedor` WRITE;
/*!40000 ALTER TABLE `proveedor` DISABLE KEYS */;
/*!40000 ALTER TABLE `proveedor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sede`
--

DROP TABLE IF EXISTS `sede`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sede` (
  `id_sede` int NOT NULL,
  `nombre` varchar(45) DEFAULT NULL,
  `estado` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id_sede`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sede`
--

LOCK TABLES `sede` WRITE;
/*!40000 ALTER TABLE `sede` DISABLE KEYS */;
INSERT INTO `sede` VALUES (1,'Sede 1',NULL),(2,'Sede 2',NULL),(3,'Sede 3',NULL),(4,'Sede 4',NULL),(5,'Sede 5',NULL),(6,'Sede 6',NULL),(7,'Sede 7',NULL),(8,'Sede 8',NULL),(9,'Sede 9',NULL),(10,'Sede 10',NULL);
/*!40000 ALTER TABLE `sede` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tarjeta`
--

DROP TABLE IF EXISTS `tarjeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tarjeta` (
  `id_tarjeta` int NOT NULL,
  `numero` int DEFAULT NULL,
  `fecha_caduca` datetime DEFAULT NULL,
  `cci` int DEFAULT NULL,
  PRIMARY KEY (`id_tarjeta`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tarjeta`
--

LOCK TABLES `tarjeta` WRITE;
/*!40000 ALTER TABLE `tarjeta` DISABLE KEYS */;
/*!40000 ALTER TABLE `tarjeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id_usuario` int NOT NULL,
  `nombres` varchar(45) NOT NULL,
  `apellidos` varchar(45) NOT NULL,
  `correo` varchar(45) NOT NULL,
  `dni` int NOT NULL,
  `codigo_colegiatura` varchar(45) DEFAULT NULL,
  `distrito` varchar(45) DEFAULT NULL,
  `seguro` varchar(45) DEFAULT NULL,
  `estado` varchar(45) DEFAULT NULL,
  `rol` varchar(45) NOT NULL,
  `contrasena` varchar(45) NOT NULL,
  `foto` blob,
  `fecha_creacion` datetime DEFAULT NULL,
  `estado_solicitud` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'Juan','Perez','juan@example.com',12345678,'CC123','Miraflores','SIS','Activo','Administrador','contraseña1',NULL,'2024-04-24 19:11:22',NULL),(2,'Maria','Lopez','maria@example.com',23456789,'CC234','San Isidro','Privado','Activo','Doctor','contraseña2',NULL,'2024-04-24 19:11:22',NULL),(3,'Carlos','Garcia','carlos@example.com',34567890,'CC345','Surco','SIS','Inactivo','Paciente','contraseña3',NULL,'2024-04-24 19:11:22',NULL),(4,'Ana','Martinez','ana@example.com',45678901,'CC456','San Borja','Privado','Activo','Farmacista','contraseña4',NULL,'2024-04-24 19:11:22','Aceptado'),(5,'Pedro','Rodriguez','pedro@example.com',56789012,'CC567','Lince','SIS','Activo','Doctor','contraseña5',NULL,'2024-04-24 19:11:22',NULL),(6,'Laura','Gomez','laura@example.com',67890123,'CC678','Barranco','Privado','Inactivo','Paciente','contraseña6',NULL,'2024-04-24 19:11:22',NULL),(7,'Diego','Hernandez','diego@example.com',78901234,'CC789','Jesus Maria','SIS','Activo','Farmacista','contraseña7',NULL,'2024-04-24 19:11:22','Pendiente'),(8,'Sofia','Diaz','sofia@example.com',89012345,'CC890','La Molina','Privado','Activo','Administrador','contraseña8',NULL,'2024-04-24 19:11:22',NULL),(9,'Julia','Sanchez','julia@example.com',90123456,'CC901','Magdalena','SIS','Inactivo','Paciente','contraseña9',NULL,'2024-04-24 19:11:22',NULL),(10,'Alejandro','Ramos','alejandro@example.com',12345670,'CC012','Chorrillos','Privado','Activo','Doctor','contraseña10',NULL,'2024-04-24 19:11:22',NULL),(11,'Elena','Fernandez','elena@example.com',23456789,'CC234','San Isidro','Privado','Activo','Paciente','contraseña11',NULL,'2024-04-24 19:11:22',NULL),(12,'Gabriel','Lopez','gabriel@example.com',34567890,'CC345','Surco','SIS','Activo','Farmacista','contraseña12',NULL,'2024-04-24 19:11:22','Rechazado'),(13,'Valeria','Martinez','valeria@example.com',45678901,'CC456','San Borja','Privado','Inactivo','Doctor','contraseña13',NULL,'2024-04-24 19:11:22',NULL),(14,'Hector','Gomez','hector@example.com',56789012,'CC567','Lince','SIS','Activo','Administrador','contraseña14',NULL,'2024-04-24 19:11:22',NULL),(15,'Camila','Rodriguez','camila@example.com',67890123,'CC678','Barranco','Privado','Activo','Doctor','contraseña15',NULL,'2024-04-24 19:11:22',NULL),(16,'Martin','Hernandez','martin@example.com',78901234,'CC789','Jesus Maria','SIS','Inactivo','Paciente','contraseña16',NULL,'2024-04-24 19:11:22',NULL),(17,'Carla','Diaz','carla@example.com',89012345,'CC890','La Molina','Privado','Activo','Farmacista','contraseña17',NULL,'2024-04-24 19:11:22','Pendiente'),(18,'Diego','Sanchez','diego@example.com',90123456,'CC901','Magdalena','SIS','Inactivo','Paciente','contraseña18',NULL,'2024-04-24 19:11:22',NULL),(19,'Lucia','Ramos','lucia@example.com',12345670,'CC012','Chorrillos','Privado','Activo','Doctor','contraseña19',NULL,'2024-04-24 19:11:22',NULL),(20,'Mateo','Perez','mateo@example.com',23456781,'CC123','Miraflores','SIS','Activo','Administrador','contraseña20',NULL,'2024-04-24 19:11:22',NULL),(21,'Natalia','Garcia','natalia@example.com',34567892,'CC234','San Isidro','Privado','Activo','Doctor','contraseña21',NULL,'2024-04-24 19:11:22',NULL),(22,'Alejandra','Lopez','alejandra@example.com',45678903,'CC345','Surco','SIS','Inactivo','Paciente','contraseña22',NULL,'2024-04-24 19:11:22',NULL),(23,'Santiago','Martinez','santiago@example.com',56789014,'CC456','San Borja','Privado','Activo','Farmacista','contraseña23',NULL,'2024-04-24 19:11:22','Aceptado'),(24,'Juana','Gomez','juana@example.com',67890125,'CC567','Lince','SIS','Inactivo','Administrador','contraseña24',NULL,'2024-04-24 19:11:22',NULL),(25,'Maximiliano','Rodriguez','maximiliano@example.com',78901236,'CC678','Barranco','Privado','Activo','Doctor','contraseña25',NULL,'2024-04-24 19:11:22',NULL),(26,'Isabella','Hernandez','isabella@example.com',89012347,'CC789','Jesus Maria','SIS','Activo','Paciente','contraseña26',NULL,'2024-04-24 19:11:22',NULL),(27,'Mariano','Diaz','mariano@example.com',90123458,'CC890','La Molina','Privado','Inactivo','Farmacista','contraseña27',NULL,'2024-04-24 19:11:22','Pendiente'),(28,'Constanza','Sanchez','constanza@example.com',12345679,'CC901','Magdalena','SIS','Activo','Administrador','contraseña28',NULL,'2024-04-24 19:11:22',NULL),(29,'Nicolas','Ramos','nicolas@example.com',23456780,'CC012','Chorrillos','Privado','Activo','Doctor','contraseña29',NULL,'2024-04-24 19:11:22',NULL),(30,'Valentina','Perez','valentina@example.com',34567891,'CC123','Miraflores','SIS','Inactivo','Paciente','contraseña30',NULL,'2024-04-24 19:11:22',NULL);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario_has_medicamentos`
--

DROP TABLE IF EXISTS `usuario_has_medicamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario_has_medicamentos` (
  `usuario_id_usuario` int NOT NULL,
  `medicamentos_id_medicamentos` int NOT NULL,
  PRIMARY KEY (`usuario_id_usuario`,`medicamentos_id_medicamentos`),
  KEY `fk_usuario_has_medicamentos_medicamentos1_idx` (`medicamentos_id_medicamentos`),
  KEY `fk_usuario_has_medicamentos_usuario1_idx` (`usuario_id_usuario`),
  CONSTRAINT `fk_usuario_has_medicamentos_medicamentos1` FOREIGN KEY (`medicamentos_id_medicamentos`) REFERENCES `medicamentos` (`id_medicamentos`),
  CONSTRAINT `fk_usuario_has_medicamentos_usuario1` FOREIGN KEY (`usuario_id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_has_medicamentos`
--

LOCK TABLES `usuario_has_medicamentos` WRITE;
/*!40000 ALTER TABLE `usuario_has_medicamentos` DISABLE KEYS */;
/*!40000 ALTER TABLE `usuario_has_medicamentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario_has_sede`
--

DROP TABLE IF EXISTS `usuario_has_sede`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario_has_sede` (
  `usuario_id_usuario` int NOT NULL,
  `sede_id_sede` int NOT NULL,
  PRIMARY KEY (`usuario_id_usuario`,`sede_id_sede`),
  KEY `fk_usuario_has_sede_sede1_idx` (`sede_id_sede`),
  KEY `fk_usuario_has_sede_usuario1_idx` (`usuario_id_usuario`),
  CONSTRAINT `fk_usuario_has_sede_sede1` FOREIGN KEY (`sede_id_sede`) REFERENCES `sede` (`id_sede`),
  CONSTRAINT `fk_usuario_has_sede_usuario1` FOREIGN KEY (`usuario_id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_has_sede`
--

LOCK TABLES `usuario_has_sede` WRITE;
/*!40000 ALTER TABLE `usuario_has_sede` DISABLE KEYS */;
/*!40000 ALTER TABLE `usuario_has_sede` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-24 14:13:36
