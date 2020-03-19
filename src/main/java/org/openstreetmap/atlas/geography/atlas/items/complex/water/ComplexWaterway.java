package org.openstreetmap.atlas.geography.atlas.items.complex.water;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openstreetmap.atlas.exception.CoreException;
import org.openstreetmap.atlas.geography.PolyLine;
import org.openstreetmap.atlas.geography.atlas.items.AtlasEntity;
import org.openstreetmap.atlas.geography.atlas.items.Line;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A waterway (usually flowing ex : rivers, streams) typically has linear geometry. This contrasts
 * with waterbody which usually refers to an area of (standing) water and typically has polygonal
 * geometry
 *
 * @author Sid
 */
public class ComplexWaterway extends ComplexWaterEntity
{
    private static final long serialVersionUID = -5567739097914423531L;

    private static final Logger logger = LoggerFactory.getLogger(ComplexWaterway.class);

    private PolyLine geometry;

    public ComplexWaterway(final AtlasEntity source, final WaterType type)
    {
        super(source, type);
    }

    @Override
    public boolean equals(final Object other)
    {
        if (other instanceof ComplexWaterbody)
        {
            final ComplexWaterbody that = (ComplexWaterbody) other;
            return new EqualsBuilder().append(this.getWaterType(), that.getWaterType())
                    .append(this.getSource(), that.getSource())
                    .append(this.getGeometry().toWkt(), that.getGeometry().toWkt()).build();
        }
        return false;
    }

    public PolyLine getGeometry()
    {
        return this.geometry;
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(this.getSource()).append(this.getWaterType())
                .append(this.getGeometry().toWkb()).build();
    }

    @Override
    protected Logger getLogger()
    {
        return logger;
    }

    @Override
    protected void populateGeometry()
    {
        final AtlasEntity source = getSource();

        /*
         * We currently don't process waterway relations. So if it is a way and it is part of
         * relation where it is a side stream, main stream or tributary, we process them.
         */
        if (source instanceof Line)
        {
            final Line line = (Line) source;
            this.geometry = line.asPolyLine();
            return;
        }
        throw new CoreException("Geometry is not set for {}", source);
    }
}
