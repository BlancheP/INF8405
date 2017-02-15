package tp1.inf8405.match3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class GridView extends View
{
    //VARIABLES FOR GRID INITIATION
    Paint paint = null;
    ArrayList[][] grid = null;
    float width = 0;
    float height = 0;
    float currentX = 0;
    float currentY = 0;
    List<Integer> colors = null;


    //VARIABLES FOR SWIPE MANAGEMENT
    float MIN_DELTA; //minimum distance in order for a swipe to take effect
    float initialPositionX = 0, initialPositionY = 0, finalPositionX, finalPositionY;

    // Variable pour la detection des matchs
    List<Integer> beginCoords = null;
    List<Integer> endCoords = null;
    BitSet statusBegin = null;
    BitSet statusEnd = null;
    List<List<List<Integer>>> matchCirclesVertical = new ArrayList<>();
    List<List<List<Integer>>> matchCirclesHorizontal = new ArrayList<>();
    List<List<Integer>> newCircles = new ArrayList<>();

    //Variable for combo management
    int numCombo = 0;

    public GridView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        paint = new Paint();
        colors = new ArrayList<>(Arrays.asList(
                Color.rgb(255, 0, 0), //red
                Color.rgb(255, 255, 0), //yellow
                Color.rgb(0, 255, 0), //green
                Color.rgb(85, 26, 139), //purple
                Color.rgb(255, 127, 0), //orange
                Color.rgb(2, 102, 200)
        ));
        matchCirclesHorizontal.add(new ArrayList<List<Integer>>());
        matchCirclesHorizontal.add(new ArrayList<List<Integer>>());
        matchCirclesVertical.add(new ArrayList<List<Integer>>());
        matchCirclesVertical.add(new ArrayList<List<Integer>>());
    }

    // Fonction qui dessine la grille
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        // Trouver le rayon des cercles
        float radius = circleRadius(((PlayActivity) getContext()).getNbCols(),
                                    ((PlayActivity) getContext()).getNbRows());

        // Lire le fichier XML et en tirer les couleurs des cercles selon le niveau
        int[] colorTable = ((PlayActivity) getContext()).getColorTable();

        int nbRows = ((PlayActivity) getContext()).getNbRows();
        int nbCols = ((PlayActivity) getContext()).getNbCols();

        // Initialiser la grille si c'est la premiere fois qu'on la dessine
        if (grid == null)
        {
            initGrid(nbRows, nbCols, getWidth(), getHeight(), colorTable);
        }

        // Appliquer un font blanc
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);

        // Dessiner tous les cercles
        for (int i = 0; i < nbRows; i ++)
        {
            for (int j = 0; j < nbCols; j++)
            {
                ArrayList<Float> coords = getCoords(i,j);
                paint.setColor(Math.round(coords.get(2)));
                canvas.drawCircle(coords.get(0), coords.get(1), radius, paint);
            }
        }
    }

    // Fonction qui est appelee pour detecter les combos une fois le match initial detecte
    protected void match()
    {
        // On verifie tous les nouveaux cercles avant que le user puisse jouer un nouveau coup
        while (newCircles.size() > 0)
        {
            statusBegin = new BitSet(2);
            // On trouve les matchs autour de ce cercle
            statusBegin = findMatch(newCircles.get(0), 0);
            if (!statusBegin.equals(new BitSet(2))) // Il y a un match
            {
                numCombo++;
                // On effectue les actions qui viennent apres la detection d'un match
                // Soit le retrait des cercles faisant partie du match et on update la grille
                doMatch(statusBegin, 0);
                // On update la grille en donnant des couleurs aleatoires aux nouveaux cercles
                updateGrid();
                // On redessine la grille eventuellement
                invalidate();
            }
            newCircles.remove(0);
        }
    }

    // Fonction qui permet de donner des couleurs aleatoires aux nouveaux cercles
    protected void updateGrid()
    {
        for (int i = 0; i < grid.length; i ++)
        {
            for (int j = 0; j < grid[i].length; j++)
            {
                ArrayList<Float> coords = getCoords(i,j);
                if (coords.size() < 3)
                {
                    grid[i][j].add((float)getRandomColor());
                }
            }
        }
    }

    // Fonction qui calcule le rayon des cercles selon la taille de la grille
    protected float circleRadius(int nbCols, int nbRows)
    {
        if(nbCols >= nbRows)
        {
            return getWidth()/nbCols/2;
        }
        else
        {
            return getHeight()/nbRows/2;
        }
    }

    // Fonction qui initialise la grille
    protected void initGrid(int nbRows, int nbCols, int w, int h, int[] colorTable)
    {
        grid = new ArrayList[nbRows][nbCols];

        width = w/(float)nbCols;
        height = h/(float)nbRows;

        MIN_DELTA = width / 2;
        currentX = currentY = width / 2;

        // Initialisation de la grille
        for (int i = 0; i < nbRows; i++)
        {
            for (int j = 0; j < nbCols; j++)
            {
                grid[i][j] = new ArrayList<>();
            }
        }

        // Pour tous les cercles, on leur assigne en 0 : leur rangee, en 1 : leur colonne et en 2 : leur couleur
        for (int i = 0; i < nbRows; i++)
        {
            for (int j = 0; j < nbCols; j++)
            {
                grid[i][j].add(currentX);
                grid[i][j].add(currentY);
                grid[i][j].add((float)colorTable[j + i * nbCols]);
                currentX += width;
            }
            currentX = width / 2;

            if(nbRows > 6)
            {
                currentY += height;
            }
            else
            {
                currentY += width;
            }
        }
    }

    // Fonction qui retourne les coordonnees du centre du cercle selon sa position dans la grille
    protected ArrayList<Float> getCoords(int i, int j)
    {
        return grid[i][j];
    }

    // Fonction qui retourne une couleur random
    protected int getRandomColor()
    {
        return colors.get((int)(Math.random() * colors.size()));
    }

    // Fonction qui detecte les swipes
    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = MotionEventCompat.getActionMasked(event);

        switch(action)
        {
            case (MotionEvent.ACTION_DOWN) :

                // On detecte le cercle initial du swipe
                initialPositionX = event.getX();
                initialPositionY = event.getY();
                beginCoords = findCircle(initialPositionX, initialPositionY);

                return true;

            case (MotionEvent.ACTION_UP) :

                // On detecte le cercle final du swipe
                finalPositionX = event.getX();
                finalPositionY = event.getY();

                // Si le swipe ne part pas d'un cercle, ne rien faire
                if (beginCoords.size() < 2)
                    return true;

                // Swipe a droite, le cercle final est donc le voisin de droite du cercle initial
                if(finalPositionX - initialPositionX >= MIN_DELTA)
                {
                    endCoords = findNeighbor(beginCoords, 2);
                }
                // Swipe a gauche, le cercle final est donc le voisin de gauche du cercle initial
                else if (Math.abs(initialPositionX - finalPositionX) >= MIN_DELTA)
                {
                    endCoords = findNeighbor(beginCoords, 3);
                }
                // Swipe en bas, le cercle final est donc le voisin du bas du cercle initial
                else if (finalPositionY - initialPositionY >= MIN_DELTA)
                {
                    endCoords = findNeighbor(beginCoords, 1);
                }
                // Swipe en haut, le cercle final est donc le voisin du haut du cercle initial
                else if (Math.abs(initialPositionY - finalPositionY) >= MIN_DELTA)
                {
                    endCoords = findNeighbor(beginCoords, 0);
                }
                // Il ne s'agit pas d'un swipe donc ne rien faire
                else
                {
                    endCoords = beginCoords;
                }

                // On echange la couleur des deux cercles
                float oldColor1 = (float)grid[beginCoords.get(0)][beginCoords.get(1)].get(2), oldColor2 = (float)grid[endCoords.get(0)][endCoords.get(1)].get(2);
                grid[beginCoords.get(0)][beginCoords.get(1)].set(2, oldColor2);
                grid[endCoords.get(0)][endCoords.get(1)].set(2, oldColor1);

                statusBegin = new BitSet(2);
                statusEnd = new BitSet(2);

                // On cherche les matchs pour les deux cercles
                statusBegin = findMatch(beginCoords, 0);
                statusEnd = findMatch(endCoords, 1);

                // Si aucun match n'est detecte, il s'agit d'une action interdite
                if (statusBegin.equals(new BitSet(2)) && statusEnd.equals(new BitSet(2)))
                {
                    // On remet les couleurs initiales aux cercles
                    grid[beginCoords.get(0)][beginCoords.get(1)].set(2, oldColor1);
                    grid[endCoords.get(0)][endCoords.get(1)].set(2, oldColor2);
                    Toast.makeText(this.getContext(), "Ceci est une action interdite!!", Toast.LENGTH_SHORT).show();
                }
                // S'il y a un match
                else
                {
                    //numCombo is set to 0 because there was a move at that moment
                    numCombo = 0;
                    // On effectue le match
                    doMatch(statusBegin, 0);
                    doMatch(statusEnd, 1);

                    ((PlayActivity) getContext()).decrementNbRemainingShots();
                    ((PlayActivity) getContext()).displayUpdatedStats();
                }

                updateGrid(); // On met la grille a jour
                invalidate(); // On redessine la grille eventuellement
                match(); // On cherche les matchs sur les nouveaux cercles de la grille

                // On gere la victoire ou la defaite du niveau
                if(((PlayActivity) getContext()).getNbRemainingShots() == 0 &&
                        (((PlayActivity) getContext()).getScore() < ((PlayActivity) getContext()).getObjective()))
                {
                    ((PlayActivity)getContext()).gameOver();
                }

                else if((((PlayActivity) getContext()).getScore() >= ((PlayActivity) getContext()).getObjective()))
                {
                    ((PlayActivity) getContext()).victory();
                }

                return true;

            default :
                return super.onTouchEvent(event);
        }

    }

    // Fonction qui affectue les actions d'un match apres la detection de celui-ci
    protected void doMatch(BitSet status, int index)
    {
        if (status.get(0) == true) // Match vertical
        {
            // On trie la liste des cercles faisant partie du match en ordre croissant de rangee afin de faciliter le traitement
            Collections.sort(matchCirclesVertical.get(index), new Comparator<List<Integer>>() {
                @Override
                public int compare(List<Integer> circle1, List<Integer> circle2) {
                    return circle2.get(0) > circle1.get(0) ? -1 : (circle2.get(0) < circle1.get(0)) ? 1 : 0;
                }
            });
            // On ajoute les cercles qui seront modifies par le match a une liste qui sera traitee plus tard
            // Soit tous ceux en haut du cercle le plus bas dans le match vertical
            int col = matchCirclesVertical.get(index).get(0).get(1);
            for (int i = matchCirclesVertical.get(index).get(matchCirclesVertical.get(index).size() - 1).get(0); i >= 0; i--)
            {
                newCircles.add(Arrays.asList(i, col));
            }
            // On retire les cercles qui font partie du match et on update ainsi la grille
            // en descendant les cercles qui etaient au dessus
            for (int i = 0; i < matchCirclesVertical.get(index).size(); i++)
            {
                bringCirclesDown(matchCirclesVertical.get(index).get(i));
            }
            // On ajoute les points correspondant au match
            ((PlayActivity) getContext()).computePoints(matchCirclesVertical.get(index).size(), numCombo);
        }
        if (status.get(1) == true) // Match horizontal
        {
            // On trie la liste des cercles faisant partie du match en ordre croissant de colonne afin de faciliter le traitement
            Collections.sort(matchCirclesHorizontal.get(index), new Comparator<List<Integer>>() {
                @Override
                public int compare(List<Integer> circle1, List<Integer> circle2) {
                    return circle2.get(1) > circle1.get(1) ? -1 : (circle2.get(1) < circle1.get(1)) ? 1 : 0;
                }
            });
            // On ajoute les cercles qui seront modifies par le match a une liste qui sera traitee plus tard
            // Soit tous ceux qui sont en haut des cercles formant une ligne horizontale
            int row = matchCirclesHorizontal.get(index).get(0).get(0);
            int minCol = matchCirclesHorizontal.get(index).get(0).get(1);
            int maxCol = matchCirclesHorizontal.get(index).get(matchCirclesHorizontal.get(index).size() - 1).get(1);
            for (int i = 0; i <= row; i++)
            {
                for (int j = minCol; j <= maxCol; j++)
                {
                    newCircles.add(Arrays.asList(i, j));
                }
            }
            // On retire les cercles qui font partie du match et on update ainsi la grille
            // en descendant les cercles qui etaient au dessus
            for (int i = 0; i < matchCirclesHorizontal.get(index).size(); i++)
            {
                bringCirclesDown(matchCirclesHorizontal.get(index).get(i));
            }
            // On ajoute les points correspondant au match
            ((PlayActivity) getContext()).computePoints(matchCirclesHorizontal.get(index).size(), numCombo);
        }

        ((PlayActivity) getContext()).displayUpdatedStats();
    }

    // Fonction qui s'execute a la suite d'un match afin de mettre a jour la grille
    // Fait descendre les cercles selon le match obtenu
    protected void bringCirclesDown(List<Integer> circle)
    {
        int col = circle.get(1);
        for (int i = circle.get(0); i > 0; i--)
        {
            if (grid[i-1][col].size() > 2)
                grid[i][col].set(2, grid[i - 1][col].get(2));
            else if (grid[i][col].size() > 2)
            {
                grid[i][col].remove(2);
            }

        }
        if (grid[0][col].size() > 2)
            grid[0][col].remove(2);
    }

    // Fonction qui permet de trouver le cercle correspond aux coordonnees x et y
    // On l'utilise lorsque le user fait un swipe dans le jeu
    protected List<Integer> findCircle(float x, float y)
    {
        List<Integer> coords = new ArrayList<>();

        // Determine la rangee correspondante
        for (int i = 0; i < grid.length; i++)
        {
            int max = Float.compare(y, ((float)grid[i][0].get(1) + width/2));
            int min = Float.compare(y, ((float)grid[i][0].get(1) - width/2));
            if (max == 0 || min == 0 || (max < 0 && min > 0) ) {
                coords.add(i);
                break;
            }

        }

        // Determine la colonne correspondante
        for (int j = 0; j < grid[0].length; j++)
        {
            int max = Float.compare(x, ((float)grid[0][j].get(0) + width/2));
            int min = Float.compare(x, ((float)grid[0][j].get(0) - width/2));
            if (max == 0 || min == 0 || (max < 0 && min > 0) ) {
                coords.add(j);
                break;
            }
        }

        return coords;
    }

    // Fonction qui determine s'il y a des matchs (horizontal ou vertical)
    // Retourne des flags qui correspondent aux differents matchs possibles
    protected BitSet findMatch(List<Integer> circle, int index)
    {
        // Initialisation des variables
        BitSet status = new BitSet(2);
        matchCirclesHorizontal.get(index).clear();
        matchCirclesVertical.get(index).clear();
        matchCirclesHorizontal.get(index).add(circle);
        matchCirclesVertical.get(index).add(circle);

        // On cherche des matchs verticaux
        constructMatch(circle, 0, index);
        constructMatch(circle, 1, index);

        // On cherche des matchs horizontaux
        constructMatch(circle, 2, index);
        constructMatch(circle, 3, index);

        // Mettre le flag s'il y a un match horizontal
        if (matchCirclesHorizontal.get(index).size() > 2)
        {
            //Toast.makeText(this.getContext(), "Horizontal : " + matchCirclesHorizontal.size(), Toast.LENGTH_SHORT).show();
            status.set(1);
        }
        // Mettre le flag s'il y a un match vertical
        if (matchCirclesVertical.get(index).size() > 2)
        {
            //Toast.makeText(this.getContext(), "Vertical : " + matchCirclesVertical.size(), Toast.LENGTH_SHORT).show();
            status.set(0);
        }
        return status;
    }

    // Fonction qui verifie les cercles voisins dans une direction donnee
    // Verifie si les couleurs sont les memes
    protected void constructMatch(List<Integer> circle, int direction, int index)
    {
        // Trouver le voisin
        List<Integer> neighbor = findNeighbor(circle, direction);
        // S'il y a un voisin et que celui-ci est de la meme couleur que le cercle etudie
        if (neighbor.size() != 0 && isThereAMatch(circle, neighbor))
        {
            // On ajoute le voisin au match
            if (direction == 0 || direction == 1)
                matchCirclesVertical.get(index).add(neighbor);
            else
                matchCirclesHorizontal.get(index).add(neighbor);
            // On appelle la fonction recursivement afin de creer la chaine des cercles constituant le match
            constructMatch(neighbor, direction, index);
        }
    }

    // Fonction qui retourne le voisin d'un cercle
    protected List<Integer> findNeighbor(List<Integer> circle, int direction)
    {
        List<Integer> coords = new ArrayList<>();
        switch (direction){
            case 0 /*up*/: if (circle.get(0) == 0) break;
                coords.add(circle.get(0) - 1);
                coords.add(circle.get(1));
                break;
            case 1 /*down*/: if (circle.get(0) == grid.length - 1) break;
                coords.add(circle.get(0) + 1);
                coords.add(circle.get(1));
                break;
            case 2 /*right*/: if (circle.get(1) == grid[0].length - 1) break;
                coords.add(circle.get(0));
                coords.add(circle.get(1) + 1);
                break;
            case 3 /*left*/: if (circle.get(1) == 0) break;
                coords.add(circle.get(0));
                coords.add(circle.get(1) - 1);
                break;
            default:
                break;
        }
        return coords;
    }

    // Fonction qui determine si deux cercles sont de meme couleur
    protected Boolean isThereAMatch(List<Integer> circle, List<Integer> neighbor)
    {
        return Math.abs((float)grid[circle.get(0)][circle.get(1)].get(2) - (float)grid[neighbor.get(0)][neighbor.get(1)].get(2)) < 0.000001;
    }

}
