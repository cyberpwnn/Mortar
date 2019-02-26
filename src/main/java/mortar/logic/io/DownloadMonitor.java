package mortar.logic.io;

import mortar.logic.io.DL.DownloadState;

@FunctionalInterface
public interface DownloadMonitor 
{
	public void onUpdate(DownloadState state, double progress, long elapsed, long estimated, long bps, long iobps, long size, long downloaded, long buffer, double bufferuse);
}
