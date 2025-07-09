import java.util.Scanner;

public class BatallaNaval {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        System.out.println("*** BATALLA NAVAL ***");
        System.out.println("Tablero 5x5");
        System.out.println("Cada jugador tiene 2 barcos");
        System.out.println();

        Tablero tableroJugador1 = new Tablero();
        Tablero tableroJugador2 = new Tablero();

        System.out.println("=== JUGADOR 1 - Coloca tus barcos ===");
        colocarBarcos(tableroJugador1, entrada);

        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

        System.out.println("=== JUGADOR 2 - Coloca tus barcos ===");
        colocarBarcos(tableroJugador2, entrada);

        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

        boolean juegoTerminado = false;
        boolean turnoJugador1 = true;

        while (!juegoTerminado) {
            if (turnoJugador1) {
                System.out.println("=== TURNO JUGADOR 1 ===");
                System.out.println("Tu tablero:");
                tableroJugador1.mostrarTablero();
                System.out.println("Tablero enemigo:");
                tableroJugador2.mostrarTableroEnemigo();

                System.out.print("Atacar coordenada (ejemplo: A1): ");
                String coordenada = entrada.next();

                boolean gano = tableroJugador2.atacar(coordenada);
                if (gano) {
                    System.out.println("¡JUGADOR 1 GANA!");
                    juegoTerminado = true;
                }
            } else {
                System.out.println("=== TURNO JUGADOR 2 ===");
                System.out.println("Tu tablero:");
                tableroJugador2.mostrarTablero();
                System.out.println("Tablero enemigo:");
                tableroJugador1.mostrarTableroEnemigo();

                System.out.print("Atacar coordenada (ejemplo: A1): ");
                String coordenada = entrada.next();

                boolean gano = tableroJugador1.atacar(coordenada);
                if (gano) {
                    System.out.println("¡JUGADOR 2 GANA!");
                    juegoTerminado = true;
                }
            }

            turnoJugador1 = !turnoJugador1;

            if (!juegoTerminado) {
                System.out.println("Presiona Enter para continuar...");
                entrada.nextLine();
                entrada.nextLine();
                System.out.println("\n\n\n\n\n\n\n\n\n\n");
            }
        }

        entrada.close();
    }

    public static void colocarBarcos(Tablero tablero, Scanner entrada) {
        System.out.println("Formato: A1 H (coordenada + orientación)");
        System.out.println("Coordenadas: A,B,C,D,E y 1,2,3,4,5");
        System.out.println("Orientación: H (horizontal) o V (vertical)");
        System.out.println();

        tablero.mostrarTablero();

        boolean barcoPuesto = false;
        while (!barcoPuesto) {
            System.out.print("Coloca tu barco de 2 casillas: ");
            String coordenada = entrada.next();
            String orientacion = entrada.next();

            barcoPuesto = tablero.colocarBarco(coordenada, orientacion, 2);
            if (barcoPuesto) {
                tablero.mostrarTablero();
            }
        }

        barcoPuesto = false;
        while (!barcoPuesto) {
            System.out.print("Coloca tu barco de 3 casillas: ");
            String coordenada = entrada.next();
            String orientacion = entrada.next();

            barcoPuesto = tablero.colocarBarco(coordenada, orientacion, 3);
            if (barcoPuesto) {
                tablero.mostrarTablero();
            }
        }

        System.out.println("¡Barcos colocados correctamente!");
    }
}

class Tablero {
    private char[][] tablero;
    private Barco[] barcos;
    private int numeroBarcos;

    public Tablero() {
        tablero = new char[5][5];
        barcos = new Barco[2]; 
        numeroBarcos = 0;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                tablero[i][j] = '~';
            }
        }
    }

    public boolean colocarBarco(String coordenada, String orientacion, int tamano) {
        char letraFila = coordenada.charAt(0);
        char numeroColumna = coordenada.charAt(1);

        int fila = letraFila - 'A';
        int columna = numeroColumna - '1';

        if (fila < 0 || fila > 4 || columna < 0 || columna > 4) {
            System.out.println("Error: Coordenada fuera del tablero");
            return false;
        }

        boolean horizontal = false;
        if (orientacion.equals("H")) {
            horizontal = true;
        } else if (orientacion.equals("V")) {
            horizontal = false;
        } else {
            System.out.println("Error: Usa H para horizontal o V para vertical");
            return false;
        }

        if (horizontal) {
            if (columna + tamano > 5) {
                System.out.println("Error: El barco no cabe horizontalmente");
                return false;
            }
        } else {
            if (fila + tamano > 5) {
                System.out.println("Error: El barco no cabe verticalmente");
                return false;
            }
        }

        for (int i = 0; i < tamano; i++) {
            int filaVerificar = horizontal ? fila : fila + i;
            int columnaVerificar = horizontal ? columna + i : columna;

            if (tablero[filaVerificar][columnaVerificar] != '~') {
                System.out.println("Error: Ya hay un barco en esa posición");
                return false;
            }
        }

        Barco nuevoBarco = new Barco(tamano, fila, columna, horizontal);
        barcos[numeroBarcos] = nuevoBarco;
        numeroBarcos++;

        for (int i = 0; i < tamano; i++) {
            int filaPoner = horizontal ? fila : fila + i;
            int columnaPoner = horizontal ? columna + i : columna;
            tablero[filaPoner][columnaPoner] = 'B';
        }

        return true;
    }

    public boolean atacar(String coordenada) {
        char letraFila = coordenada.charAt(0);
        char numeroColumna = coordenada.charAt(1);

        int fila = letraFila - 'A';
        int columna = numeroColumna - '1';

        if (fila < 0 || fila > 4 || columna < 0 || columna > 4) {
            System.out.println("Coordenada inválida");
            return false;
        }

        char posicion = tablero[fila][columna];

        if (posicion == 'B') {
            tablero[fila][columna] = 'X';
            System.out.println("¡IMPACTO!");

            for (int i = 0; i < numeroBarcos; i++) {
                if (barcos[i].estEnPosicion(fila, columna)) {
                    barcos[i].recibirImpacto();
                    if (barcos[i].estaHundido()) {
                        System.out.println("¡BARCO HUNDIDO!");
                    }
                    break;
                }
            }

            boolean todosHundidos = true;
            for (int i = 0; i < numeroBarcos; i++) {
                if (!barcos[i].estaHundido()) {
                    todosHundidos = false;
                    break;
                }
            }

            return todosHundidos;

        } else if (posicion == '~') {
            tablero[fila][columna] = 'O';
            System.out.println("¡AGUA!");
            return false;
        } else {
            System.out.println("Ya atacaste esta posición");
            return false;
        }
    }

    public void mostrarTablero() {
        System.out.println("   1 2 3 4 5");
        for (int i = 0; i < 5; i++) {
            char letra = (char)('A' + i);
            System.out.print(letra + " ");
            for (int j = 0; j < 5; j++) {
                System.out.print(" " + tablero[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public void mostrarTableroEnemigo() {
        System.out.println("   1 2 3 4 5");
        for (int i = 0; i < 5; i++) {
            char letra = (char)('A' + i);
            System.out.print(letra + " ");
            for (int j = 0; j < 5; j++) {
                char celda = tablero[i][j];
                if (celda == 'B') {
                    celda = '~';
                }
                System.out.print(" " + celda);
            }
            System.out.println();
        }
        System.out.println();
    }
}

class Barco {
    private int tamano;
    private int fila;
    private int columna;
    private boolean horizontal;
    private int impactos;

    public Barco(int tamano, int fila, int columna, boolean horizontal) {
        this.tamano = tamano;
        this.fila = fila;
        this.columna = columna;
        this.horizontal = horizontal;
        this.impactos = 0;
    }

    public boolean estEnPosicion(int filaAtaque, int columnaAtaque) {
        if (horizontal) {
            if (filaAtaque == fila && columnaAtaque >= columna && columnaAtaque < columna + tamano) {
                return true;
            }
        } else {
            if (columnaAtaque == columna && filaAtaque >= fila && filaAtaque < fila + tamano) {
                return true;
            }
        }
        return false;
    }

    public void recibirImpacto() {
        impactos++;
    }

    public boolean estaHundido() {
        return impactos >= tamano;
    }
}
