#!/usr/bin/env python3

import sys
import os

try:
    import oracledb
except ImportError:
    print("❌ Módulo oracledb no instalado")
    print("Instalando...")
    os.system("pip3 install oracledb")
    import oracledb

def ejecutar_script_sql(cursor, archivo, connection):
    print(f"\n{'='*60}")
    print(f"Ejecutando: {archivo}")
    print('='*60)
    
    try:
        with open(archivo, 'r', encoding='utf-8') as f:
            contenido = f.read()
        
        bloques = []
        bloque_actual = []
        en_bloque_plsql = False
        
        for linea in contenido.split('\n'):
            linea_limpia = linea.strip()
            
            if linea_limpia.upper().startswith(('CREATE OR REPLACE TRIGGER', 'CREATE TRIGGER', 
                                                  'CREATE OR REPLACE PROCEDURE', 'CREATE PROCEDURE',
                                                  'CREATE OR REPLACE FUNCTION', 'CREATE FUNCTION')):
                en_bloque_plsql = True
                bloque_actual = [linea]
            elif en_bloque_plsql:
                bloque_actual.append(linea)
                if linea_limpia == '/' or (linea_limpia.endswith(';') and 'END' in linea_limpia.upper()):
                    bloques.append('\n'.join(bloque_actual))
                    bloque_actual = []
                    en_bloque_plsql = False
            elif linea_limpia and not linea_limpia.startswith('--'):
                bloque_actual.append(linea)
                if linea_limpia.endswith(';'):
                    statement = '\n'.join(bloque_actual).strip()
                    if statement and statement != ';':
                        bloques.append(statement)
                    bloque_actual = []
        
        exitosos = 0
        errores = 0
        
        for i, statement in enumerate(bloques, 1):
            statement = statement.strip().rstrip(';')
            if not statement or statement == '/':
                continue
                
            try:
                cursor.execute(statement)
                exitosos += 1
                
                if 'CREATE' in statement.upper():
                    objeto = statement.split()[1:4]
                    print(f"  ✓ {' '.join(objeto)}")
                elif 'INSERT' in statement.upper():
                    print(f"  ✓ INSERT ejecutado")
                    
            except oracledb.DatabaseError as e:
                error_obj, = e.args
                if error_obj.code == 955:
                    print(f"  ⚠ Objeto ya existe (omitiendo)")
                elif error_obj.code == 1:
                    print(f"  ⚠ Registro duplicado (omitiendo)")
                else:
                    print(f"  ❌ Error: {error_obj.message}")
                    errores += 1
        
        connection.commit()
        print(f"\n✅ Completado: {exitosos} operaciones exitosas")
        if errores > 0:
            print(f"⚠️  {errores} errores no críticos")
        
        return True
        
    except Exception as e:
        print(f"❌ Error al procesar {archivo}: {e}")
        return False

def main():
    print("═"*60)
    print("  EJECUTAR SCRIPTS EN ORACLE CLOUD")
    print("═"*60)
    
    wallet_path = os.path.abspath("wallet")
    
    if not os.path.exists(wallet_path):
        print("\n❌ Error: Carpeta wallet no encontrada")
        print("   Esperada en: " + wallet_path)
        sys.exit(1)
    
    print(f"\n📁 Wallet encontrado: {wallet_path}")
    
    user = "biblioteca"
    password = "#Ng3naUQa*THhTd"
    service = "s58onuxcx4c1qxe9_high"
    
    print(f"👤 Usuario: {user}")
    print(f"🔌 Service: {service}")
    print(f"📍 Wallet: wallet/")
    
    try:
        print("\n🔄 Conectando a Oracle Cloud...")
        
        ewallet_path = os.path.join(wallet_path, "ewallet.pem")
        cwallet_path = os.path.join(wallet_path, "cwallet.sso")
        
        if not os.path.exists(ewallet_path) and not os.path.exists(cwallet_path):
            print(f"❌ Error: Archivos del wallet no encontrados")
            sys.exit(1)
        
        host = "adb.sa-santiago-1.oraclecloud.com"
        port = 1522
        service_name = "g64afca1579a0d2_s58onuxcx4c1qxe9_high.adb.oraclecloud.com"
        
        connection = oracledb.connect(
            user=user,
            password=password,
            host=host,
            port=port,
            service_name=service_name,
            wallet_location=wallet_path,
            wallet_password=""
        )
        
        print("✅ Conexión exitosa a Oracle Cloud")
        
        cursor = connection.cursor()
        
        print("\n📊 Verificando conexión...")
        cursor.execute("SELECT 'Conectado a Oracle Cloud' FROM DUAL")
        result = cursor.fetchone()
        print(f"   {result[0]}")
        
        if ejecutar_script_sql(cursor, 'database/schema.sql', connection):
            print("\n✅ Schema creado exitosamente")
        else:
            print("\n❌ Error al crear schema")
            sys.exit(1)
        
        if ejecutar_script_sql(cursor, 'database/seed.sql', connection):
            print("\n✅ Datos cargados exitosamente")
        else:
            print("\n❌ Error al cargar datos")
            sys.exit(1)
        
        print("\n" + "═"*60)
        print("  VERIFICACIÓN DE DATOS")
        print("═"*60)
        
        cursor.execute("SELECT COUNT(*) FROM usuarios")
        count_usuarios = cursor.fetchone()[0]
        print(f"  📊 Usuarios en BD: {count_usuarios}")
        
        cursor.execute("SELECT COUNT(*) FROM libros")
        count_libros = cursor.fetchone()[0]
        print(f"  📚 Libros en BD: {count_libros}")
        
        cursor.execute("SELECT COUNT(*) FROM prestamos")
        count_prestamos = cursor.fetchone()[0]
        print(f"  📋 Préstamos en BD: {count_prestamos}")
        
        print("\n  Ejemplo de datos:")
        cursor.execute("SELECT nombre, apellido, email FROM usuarios WHERE ROWNUM <= 3")
        for row in cursor:
            print(f"    • {row[0]} {row[1]} - {row[2]}")
        
        cursor.close()
        connection.close()
        
        print("\n" + "═"*60)
        print("  ✅ BASE DE DATOS CONFIGURADA Y LISTA")
        print("═"*60)
        print("\nAhora puedes:")
        print("  1. Iniciar las funciones serverless: docker-compose up")
        print("  2. Probar las APIs con Postman")
        print("  3. Verificar integración completa")
        
    except oracledb.Error as e:
        error_obj, = e.args
        print(f"\n❌ Error de Oracle: {error_obj.message}")
        print(f"   Código: {error_obj.code}")
        sys.exit(1)
    except Exception as e:
        print(f"\n❌ Error: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)

if __name__ == "__main__":
    main()
