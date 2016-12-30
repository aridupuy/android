package com.cobrodigital.com.cobrodigital2.Listeners;

import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class EndScroll implements OnScrollListener {
    private onScrollEndListener onScrollEnd;
    private int visibles = 15;
    private int paginaActual = 0;
    private int totalAnterior = 0;
    private boolean cargando = true;
    public interface onScrollEndListener{void onEnd(int pagina);}
    public EndScroll(onScrollEndListener onScrollEnd) {
        super();
        this.onScrollEnd = onScrollEnd;
    }
    public EndScroll(int visibleThreshold, onScrollEndListener onScrollEnd) {
        super();
        this.visibles = visibleThreshold;
        this.onScrollEnd = onScrollEnd;
    }
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (cargando) {
            if (totalItemCount > totalAnterior) {
                cargando = false;
                totalAnterior = totalItemCount;
                paginaActual++;
            }
        }
        if (!cargando && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibles)) {
            cargando = true;
            if(totalItemCount > visibles)
                onScrollEnd.onEnd(paginaActual);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }



}
