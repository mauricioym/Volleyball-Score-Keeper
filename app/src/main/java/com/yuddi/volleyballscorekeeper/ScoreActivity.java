package com.yuddi.volleyballscorekeeper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {

    private TeamPoints team1Points;
    private TeamPoints team2Points;

    private List<Integer> undoStack = new ArrayList<>();
    private final int MAX_UNDO_STACK = 10;
    private Button undoButton;
    private View mainBoard;

    private String winner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        TextView total_textview = (TextView) findViewById(R.id.total_points_text_view_1);
        TextView[] stats_textviews_1 = new TextView[]{
                (TextView) findViewById(R.id.spike_text_view_1),
                (TextView) findViewById(R.id.block_text_view_1),
                (TextView) findViewById(R.id.serve_text_view_1),
                (TextView) findViewById(R.id.op_error_text_view_1)
        };
        team1Points = new TeamPoints(total_textview, stats_textviews_1);

        TextView total_textview2 = (TextView) findViewById(R.id.total_points_text_view_2);
        TextView[] stats_textviews_2 = new TextView[]{
                (TextView) findViewById(R.id.spike_text_view_2),
                (TextView) findViewById(R.id.block_text_view_2),
                (TextView) findViewById(R.id.serve_text_view_2),
                (TextView) findViewById(R.id.op_error_text_view_2)
        };
        team2Points = new TeamPoints(total_textview2, stats_textviews_2);

        mainBoard = findViewById(R.id.main_table);
        undoButton = (Button) findViewById(R.id.undo_button);
    }

    public void increment(View view) {
        switch (view.getId()) {
            case R.id.spike_button_1:
                team1Points.increment(TeamPoints.SPIKE);
                break;
            case R.id.block_button_1:
                team1Points.increment(TeamPoints.BLOCK);
                break;
            case R.id.serve_button_1:
                team1Points.increment(TeamPoints.SERVE);
                break;
            case R.id.op_error_button_1:
                team1Points.increment(TeamPoints.OP_ERRORS);
                break;
            case R.id.spike_button_2:
                team2Points.increment(TeamPoints.SPIKE);
                break;
            case R.id.block_button_2:
                team2Points.increment(TeamPoints.BLOCK);
                break;
            case R.id.serve_button_2:
                team2Points.increment(TeamPoints.SERVE);
                break;
            case R.id.op_error_button_2:
                team2Points.increment(TeamPoints.OP_ERRORS);
                break;
            default:
                return;
        }

        addToStack(view.getId());
        checkForWinner();
    }

    private void addToStack(int clickedButtonId) {
        undoStack.add(clickedButtonId);
        if (undoStack.size() > MAX_UNDO_STACK) {
            undoStack.remove(0);
        }

        if (!undoButton.isEnabled()) {
            undoButton.setEnabled(true);
        }
    }

    private void checkForWinner() {
        int team1Score = team1Points.getTotal();
        int team2Score = team2Points.getTotal();
        if (team1Score >= 25 && team1Score - team2Score >= 2) {
            winner = getResources().getString(R.string.team1_name);
        } else if (team2Score >= 25 && team2Score - team1Score >= 2) {
            winner = getResources().getString(R.string.team2_name);
        }
        if (winner != null) {
            Toast.makeText(ScoreActivity.this, winner + " won the set", Toast.LENGTH_LONG).show();
            setPlusButtonsEnabled(false, mainBoard);
        }
    }

    private void setPlusButtonsEnabled(boolean enabled, View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                setPlusButtonsEnabled(enabled, viewGroup.getChildAt(i));
            }
        } else if (view instanceof Button) {
            view.setEnabled(enabled);
        }
    }

    public void undo(View view) {
        if (undoStack.size() > 0) {
            if (winner != null) {
                setPlusButtonsEnabled(true, mainBoard);
                winner = null;
            }

            int lastPlusButtonClickedId = undoStack.remove(undoStack.size() - 1);
            switch (lastPlusButtonClickedId) {
                case R.id.spike_button_1:
                    team1Points.decrement(TeamPoints.SPIKE);
                    break;
                case R.id.block_button_1:
                    team1Points.decrement(TeamPoints.BLOCK);
                    break;
                case R.id.serve_button_1:
                    team1Points.decrement(TeamPoints.SERVE);
                    break;
                case R.id.op_error_button_1:
                    team1Points.decrement(TeamPoints.OP_ERRORS);
                    break;
                case R.id.spike_button_2:
                    team2Points.decrement(TeamPoints.SPIKE);
                    break;
                case R.id.block_button_2:
                    team2Points.decrement(TeamPoints.BLOCK);
                    break;
                case R.id.serve_button_2:
                    team2Points.decrement(TeamPoints.SERVE);
                    break;
                case R.id.op_error_button_2:
                    team2Points.decrement(TeamPoints.OP_ERRORS);
                    break;
                default:
                    return;
            }

            if (undoStack.size() == 0) {
                undoButton.setEnabled(false);
            }
        }
    }

    public void reset(View view) {
        team1Points.reset();
        team2Points.reset();
        setPlusButtonsEnabled(true, mainBoard);
        undoStack.clear();
        undoButton.setEnabled(false);
        winner = null;
    }
}
