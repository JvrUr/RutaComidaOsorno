import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.linear_model import LinearRegression
from sklearn.metrics import r2_score

# Cargar datos
df = pd.read_csv('TiempoComentarios.csv')

# Variables
X = df[['Largo']].values
y = df['Tiempo'].values

# Modelo
model = LinearRegression()
model.fit(X, y)

# Predicciones
y_pred = model.predict(X)

# Coeficientes
m = model.coef_[0]
b = model.intercept_
r2 = r2_score(y, y_pred)

print(f"Ecuación del modelo: Tiempo = {b:.4f} + {m:.4f} * Largo")
print(f"Bondad del modelo (R^2): {r2:.4f}")

# Predicciones específicas
para_predecir = [245, 37, 119]
for val in para_predecir:
    pred = model.predict([[val]])[0]
    print(f"Tiempo estimado para largo {val}: {pred:.4f}")

# Diagrama de dispersión
plt.figure(figsize=(8, 6))
plt.scatter(X, y, color='blue', label='Datos reales')
plt.plot(X, y_pred, color='red', label='Línea de regresión')
plt.title('Diagrama de Dispersión: Largo vs Tiempo del Comentario')
plt.xlabel('Largo del comentario (caracteres o palabras)')
plt.ylabel('Tiempo (segundos)')
plt.legend()
plt.grid(True)
plt.savefig('diagrama_dispersion.png')
print("Gráfico guardado como diagrama_dispersion.png")
