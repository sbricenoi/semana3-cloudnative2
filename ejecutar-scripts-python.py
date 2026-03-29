#!/usr/bin/env python3

import os
import sys

try:
    import oracledb
except ImportError:
    print("❌ Error: Módulo oracledb no instalado")
    print("Instalar con: pip install oracledb")
    sys.exit(1)

def cargar_env():
    env_vars = {}
    try:
        with open('.env', 'r') as f:
            for line in f:
                line = line.strip()
                if line and not line.startswith('#'):
                    if '=' in line:
                        key, value = line.split('=', 1)
                        env_vars[key] = value
    except FileNotFoundError:
        print("❌ Archivo .env no encontrado")
        sys.exit(1)
    return env_vars

def extraer_conexion(db_url):
    if 'jdbc:oracle:thin:@' in db_url:
        db_url = db_url.replace('jdbc:oracle:thin:@', '')
        if '/' in db_url:
            host_port, service = db_url.split('/', 1)
            if ':' in host_port:
                host, port = host_port.split(':', 1)
                return host, int(port), service
    return None, None, None

def ejecutar_script(cursor, archivo):
    print(f"Ejecutando {archivo}...")
    try:
        with open(archivo, 'r', encoding='utf-8') as f:
            contenido = f.read()
            
        statements = contenido.split(';')
        for stmt in statements:
            stmt = stmt.strip()
            if stmt and not stmt.startswith('--'):
                try:
                    cursor.execute(stmt)
                except Exception as e:
                    if 'already exists' not in str(e).lower():
                        print(f"  Advertencia: {e}")
        
        print(f"✓ {archivo} ejecutado exitosamente")
        return True
    except Exception as e:
        print(f"❌ Error al ejecutar {archivo}: {e}")
        return False

def main():
    print("═" * 60)
    print("  Ejecutar Scripts en Oracle Cloud - Python")
    print("═" * 60)
    print()
    
    env = cargar_env()
    
    db_url = env.get('DB_URL', '')
    db_user = env.get('DB_USER', '')
    db_password = env.get('DB_PASSWORD', '')
    
    if '<COMPLETAR' in db_url:
        print("❌ Error: Debes completar las credenciales en .env")
        print("   Edita .env y proporciona host, puerto y service name")
        sys.exit(1)
    
    host, port, service = extraer_conexion(db_url)
    
    if not all([host, port, service]):
        print("❌ Error: No se pudo extraer información de DB_URL")
        print(f"   DB_URL actual: {db_url}")
        sys.exit(1)
    
    print(f"Usuario: {db_user}")
    print(f"Host: {host}")
    print(f"Puerto: {port}")
    print(f"Service: {service}")
    print()
    
    print("Conectando a Oracle Cloud...")
    
    try:
        dsn = oracledb.makedsn(host, port, service_name=service)
        connection = oracledb.connect(
            user=db_user,
            password=db_password,
            dsn=dsn
        )
        
        print("✓ Conexión exitosa")
        print()
        
        cursor = connection.cursor()
        
        if ejecutar_script(cursor, 'database/schema.sql'):
            connection.commit()
        
        print()
        
        if ejecutar_script(cursor, 'database/seed.sql'):
            connection.commit()
        
        print()
        print("Verificando datos insertados...")
        
        cursor.execute("SELECT COUNT(*) FROM usuarios")
        count_usuarios = cursor.fetchone()[0]
        print(f"  Usuarios: {count_usuarios}")
        
        cursor.execute("SELECT COUNT(*) FROM libros")
        count_libros = cursor.fetchone()[0]
        print(f"  Libros: {count_libros}")
        
        cursor.execute("SELECT COUNT(*) FROM prestamos")
        count_prestamos = cursor.fetchone()[0]
        print(f"  Préstamos: {count_prestamos}")
        
        cursor.close()
        connection.close()
        
        print()
        print("═" * 60)
        print("  ✓ Scripts ejecutados correctamente")
        print("═" * 60)
        
    except oracledb.Error as e:
        print(f"❌ Error de Oracle: {e}")
        sys.exit(1)
    except Exception as e:
        print(f"❌ Error: {e}")
        sys.exit(1)

if __name__ == "__main__":
    main()
