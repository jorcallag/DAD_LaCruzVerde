-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: daddatabase
-- ------------------------------------------------------
-- Server version	8.0.19

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
-- Table structure for table `actuador`
--

DROP TABLE IF EXISTS `actuador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actuador` (
  `id_actuador` int NOT NULL AUTO_INCREMENT,
  `tipo` varchar(45) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `id_dispositivo` int DEFAULT NULL,
  PRIMARY KEY (`id_actuador`),
  KEY `actuador_dispositivo_idx` (`id_dispositivo`),
  CONSTRAINT `actuador_dispositivo` FOREIGN KEY (`id_dispositivo`) REFERENCES `dispositivo` (`id_dispositivo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actuador`
--

LOCK TABLES `actuador` WRITE;
/*!40000 ALTER TABLE `actuador` DISABLE KEYS */;
INSERT INTO `actuador` VALUES (1,'bomba','bomba_aloevera',1),(2,'luz_normal','luz_normal_aloevera',1),(3,'luz_ultravioleta','luz_ultravioleta_aloevera',1),(4,'resistencia','resistencia_aloevera',1),(5,'pulverizador','pulverizador_aloevera',1),(6,'ventilador','ventilador_aloevera',1);
/*!40000 ALTER TABLE `actuador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `actuador_valor`
--

DROP TABLE IF EXISTS `actuador_valor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actuador_valor` (
  `id_actuador_valor` int NOT NULL AUTO_INCREMENT,
  `id_actuador` int NOT NULL,
  `on` tinyint NOT NULL,
  `timestamp` bigint DEFAULT NULL,
  PRIMARY KEY (`id_actuador_valor`),
  KEY `actuador_valor_actuador_idx` (`id_actuador`),
  CONSTRAINT `actuador_valor_actuador` FOREIGN KEY (`id_actuador`) REFERENCES `actuador` (`id_actuador`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actuador_valor`
--

LOCK TABLES `actuador_valor` WRITE;
/*!40000 ALTER TABLE `actuador_valor` DISABLE KEYS */;
INSERT INTO `actuador_valor` VALUES (1,1,1,123456789),(2,1,1,112345674),(3,1,1,123443333),(4,1,1,123666665),(5,1,1,129845356),(6,1,1,123534554);
/*!40000 ALTER TABLE `actuador_valor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dispositivo`
--

DROP TABLE IF EXISTS `dispositivo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dispositivo` (
  `id_dispositivo` int NOT NULL,
  `ip` varchar(45) DEFAULT NULL,
  `nombre` varchar(45) DEFAULT NULL,
  `id_planta` int NOT NULL,
  `initialTimestamp` bigint DEFAULT NULL,
  PRIMARY KEY (`id_dispositivo`),
  KEY `dispositivo_planta_idx` (`id_planta`),
  CONSTRAINT `dispositivo_planta` FOREIGN KEY (`id_planta`) REFERENCES `planta` (`id_planta`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dispositivo`
--

LOCK TABLES `dispositivo` WRITE;
/*!40000 ALTER TABLE `dispositivo` DISABLE KEYS */;
INSERT INTO `dispositivo` VALUES (1,'192.168.0.108','CruzVerde_Aloevera',1,123456789),(2,'150.215.489.215.114.056','CruzVerde_Romero',3,123434567),(3,'150.123.123.123','CruzVerde_Rosal',2,121233456);
/*!40000 ALTER TABLE `dispositivo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `planta`
--

DROP TABLE IF EXISTS `planta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `planta` (
  `id_planta` int NOT NULL AUTO_INCREMENT,
  `nombre_planta` varchar(45) NOT NULL,
  `temp_amb_plata` float NOT NULL,
  `humed_tierra_planta` float NOT NULL,
  `humed_amb_planta` float NOT NULL,
  PRIMARY KEY (`id_planta`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `planta`
--

LOCK TABLES `planta` WRITE;
/*!40000 ALTER TABLE `planta` DISABLE KEYS */;
INSERT INTO `planta` VALUES (1,'Aloevera',22,25,5),(2,'Rosal',20,65,5),(3,'Romero',27,25,2);
/*!40000 ALTER TABLE `planta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sensor`
--

DROP TABLE IF EXISTS `sensor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sensor` (
  `id_sensor` int NOT NULL AUTO_INCREMENT,
  `tipo` varchar(45) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `id_dispositivo` int DEFAULT NULL,
  PRIMARY KEY (`id_sensor`),
  KEY `sensor_dispositivo_idx` (`id_dispositivo`),
  CONSTRAINT `sensor_dispositivo` FOREIGN KEY (`id_dispositivo`) REFERENCES `dispositivo` (`id_dispositivo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=339 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensor`
--

LOCK TABLES `sensor` WRITE;
/*!40000 ALTER TABLE `sensor` DISABLE KEYS */;
INSERT INTO `sensor` VALUES (1,'temp_amb','temp_amb_rosal',3),(2,'humed_amb','humed_amb_rosal',3),(3,'humed_tierra','humed_tier_rosal',3),(4,'temp_amb','temp_amb_romero',2),(5,'humed_amb','humed_amb_romero',2),(6,'humed_tierra','humed_tier_romero',2),(7,'temp_amb','temp_amb_aloevera',1),(8,'humed_amb','humed_amb_aloevera',1),(9,'humed_tierra','humed_tierra_aloevera',1),(10,'liquido_tanque','tanque_aloevera',1);
/*!40000 ALTER TABLE `sensor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sensor_valor`
--

DROP TABLE IF EXISTS `sensor_valor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sensor_valor` (
  `id_sensor_valor` int NOT NULL AUTO_INCREMENT,
  `id_sensor` int NOT NULL,
  `valor` float NOT NULL,
  `precision` float NOT NULL,
  `timestamp` bigint DEFAULT NULL,
  PRIMARY KEY (`id_sensor_valor`),
  KEY `sensor_valor_sensor_idx` (`id_sensor`),
  CONSTRAINT `sensor_valor_sensor` FOREIGN KEY (`id_sensor`) REFERENCES `sensor` (`id_sensor`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensor_valor`
--

LOCK TABLES `sensor_valor` WRITE;
/*!40000 ALTER TABLE `sensor_valor` DISABLE KEYS */;
INSERT INTO `sensor_valor` VALUES (1,1,32,2,122345654),(2,2,21,2,123456787),(3,3,50,2,123234567),(4,4,23,2,123434567),(5,5,45,2,125646784),(6,6,75,2,126545789),(7,7,12,2,123213457),(8,8,35,2,128667975),(9,9,40,2,128654567);
/*!40000 ALTER TABLE `sensor_valor` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-03-19 14:13:32
